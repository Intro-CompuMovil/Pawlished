import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

class ViewNearestActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_nearest)

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        val peluquerias = arrayOf(
            "el mundo de la mascota\nDirección: Calle Falsa 123\nHorario: 9:00 AM - 6:00 PM",
            "tu perro feliz\nDirección: Avenida Imaginaria 456\nHorario: 10:00 AM - 7:00 PM",
            "el gato felix \nDirección: Plaza Inexistente 789\nHorario: 8:00 AM - 5:00 PM"
        )

        val listView: ListView = findViewById(R.id.peluqueriasListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, peluquerias)
        listView.adapter = adapter

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Solicitar permiso de ubicación
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        if (EasyPermissions.hasPermissions(this, locationPermission)) {
            // El permiso ya está concedido, puedes mostrar el mapa real aquí.
            val mapImageView = findViewById<ImageView>(R.id.mapImageView)
            mapImageView.setImageResource(R.drawable.real_map_image)
        } else {
            // El permiso no está concedido, solicítalo.
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, 1, locationPermission)
                    .setRationale("Necesitamos tu ubicación para mostrar el mapa.")
                    .setPositiveButtonText("Aceptar")
                    .setNegativeButtonText("Cancelar")
                    .setTheme(R.style.AppTheme)
                    .build()
            )
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            // Obtén la peluquería seleccionada
            val selectedPeluqueria = peluquerias[position]

            // Obtén los servicios seleccionados que pasaste desde SolicitarCorteActivity
            val serviciosSeleccionados = intent.getStringArrayListExtra("servicios_seleccionados")

            // Iniciar ViewStateActivity y pasar los datos
            val intent = Intent(this, ViewStateActivity::class.java)
            intent.putStringArrayListExtra("servicios_seleccionados", serviciosSeleccionados)
            intent.putExtra("selected_peluqueria", selectedPeluqueria)
            startActivity(intent)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        // El usuario concedió permisos, muestra el mapa real aquí.
        val mapImageView = findViewById<ImageView>(R.id.mapImageView)
        mapImageView.setImageResource(R.drawable.real_map_image)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        // El usuario denegó permisos, muestra un mensaje o realiza una acción apropiada.
        // Por ejemplo, puedes mostrar un mensaje indicando que el mapa no se mostrará sin permisos.
    }
}
