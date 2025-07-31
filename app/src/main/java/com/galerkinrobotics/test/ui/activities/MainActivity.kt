package com.galerkinrobotics.test.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.savedstate.SavedState
import com.galerkinrobotics.test.R
import com.galerkinrobotics.test.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() ,NavController.OnDestinationChangedListener{
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Navigation setup
        setupNavigation()
        
        // Toolbar'ı action bar olarak ayarla
        setSupportActionBar(binding.toolbar)
        
        // Varsayılan başlığı kaldır (özel TextView kullanıyoruz)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        // Özel geri butonuna click listener
        binding.backButton.setOnClickListener {
            navController.navigateUp()
        }
        
        // Ayarlar butonuna click listener
        binding.settingsButton.setOnClickListener {
            // TODO: Ayarlar sayfasına yönlendir
            // Bu kısım ayarlar fragment'ı veya activity'si oluşturulduğunda implement edilecek
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: SavedState?
    ) {
        when(destination.id){
            R.id.loginFragment -> {
                binding.toolbarTitle.text = "İnoHom Giriş"
                binding.settingsButton.visibility = View.GONE
                binding.backButton.visibility = View.GONE
            }
            R.id.inohomFragment -> {
                binding.toolbarTitle.text = "İnoHom"
                binding.settingsButton.visibility = View.VISIBLE
                binding.backButton.visibility = View.GONE
            }
            R.id.aydınlatmaFragment -> {
                binding.toolbarTitle.text = "Aydınlatma"
                binding.settingsButton.visibility = View.VISIBLE
                binding.backButton.visibility = View.VISIBLE
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Geri tuşuna basıldığında navigation ile geri git
                navController.navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(this)
    }
}