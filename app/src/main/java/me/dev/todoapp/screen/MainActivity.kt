package me.dev.todoapp.screen

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import me.dev.todoapp.R
import me.dev.todoapp.databinding.ActivityMainBinding
import me.dev.todoapp.screen.task.dialog.addtask.AddTaskDialogFragment
import me.dev.todoapp.screen.task.dialog.addtask.IAddTaskListener
import me.dev.todoapp.utils.CallbackResult

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var fab: FloatingActionButton
    private lateinit var navDrawer: NavigationView
    private lateinit var navHost: NavHostFragment

    private val viewModel by viewModels<MainActivityViewModel> {
        MainActivityViewModel.Factory(
            application
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        setupBottomNav()
        setupNavDrawer()
        binding.fab.setOnClickListener { fabOnClick() }
    }

    private fun initUI() {
        bottomNav = binding.bottomNav
        fab = binding.fab
        navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navDrawer = binding.navDrawer
    }

    private fun setupNavDrawer() {
        val headerTitle =
            navDrawer.getHeaderView(0).findViewById<TextView>(R.id.drawer_header_title)
        headerTitle.text = getString(R.string.hello_s, "User")
        viewModel.getAllCategory().observe(this) { list ->
            // Create menu
            navDrawer.menu.clear()
            navDrawer.inflateMenu(R.menu.drawer_menu)
            val menu: Menu = navDrawer.menu
            menu.add(R.id.drawer_category_menu, 0, 0, getString(R.string.all)).apply {
                setIcon(R.drawable.ic_list_alt)
                isCheckable = true
            }
            list.forEach { category ->
                menu.add(R.id.drawer_category_menu, category.id, 0, category.name).apply {
                    setIcon(R.drawable.ic_list_alt)
                    isCheckable = true
                }
            }
            // Handle selected event
            navDrawer.setNavigationItemSelectedListener {
                binding.root.close()
                when (it.itemId) {
                    R.id.drawer_donate_menu -> {
                        Toast.makeText(
                            applicationContext,
                            "This feature is not available at the moment!",
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }

                    R.id.drawer_info_menu -> {
                        Toast.makeText(
                            applicationContext,
                            "This feature is not available at the moment!",
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }

                    R.id.drawer_setting_menu -> {
                        Toast.makeText(
                            applicationContext,
                            "This feature is not available at the moment!",
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }

                    R.id.drawer_theme_menu -> {
                        Toast.makeText(
                            applicationContext,
                            "This feature is not available at the moment!",
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }

                    else -> {
                        viewModel.setCategoryFilter(it.itemId, this)
                        if (navHost.navController.currentDestination?.id != R.id.allTaskFragment) {
                            navHost.navController.navigate(R.id.allTaskFragment)
                        }
                        true
                    }
                }
            }
            viewModel.categoryId.observe(this) observerCategoryId@{ id ->
                val item: MenuItem = navDrawer.menu.findItem(id) ?: return@observerCategoryId
                if (item.isChecked) return@observerCategoryId
                else item.isChecked = true
            }
        }
    }

    private fun setupBottomNav() {
        bottomNav.setupWithNavController(navHost.navController)
        bottomNav.setOnItemSelectedListener {
            if (it.itemId == R.id.menuBtn) {
                openNavDrawer()
                false
            } else {
                onNavDestinationSelected(it, navHost.navController)
            }
        }
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            // FAB Visibility
            if (destination.id == R.id.allTaskFragment || destination.id == R.id.calendarFragment) {
                fab.visibility = View.VISIBLE
            } else {
                fab.visibility = View.GONE
            }
            // Bottom Nav Visibility
            if (destination.id == R.id.allTaskFragment || destination.id == R.id.calendarFragment || destination.id == R.id.mineFragment) {
                binding.bottomNav.visibility = View.VISIBLE
            } else {
                binding.bottomNav.visibility = View.GONE
            }
        }
    }

    private fun openNavDrawer() {
        binding.root.open()
        onBackPressedDispatcher.addCallback(this) {
            if (binding.root.isDrawerOpen(GravityCompat.START)) {
                binding.root.close()
                this.remove()
            } else {
                this.remove()
                onBackPressed()
            }
        }
    }

    //
    private fun showAddTaskDialog() {
        val dialog = AddTaskDialogFragment.newInstance(object : IAddTaskListener {
            override fun onFinish(result: Int, categoryId: Int?) {
                if (result == CallbackResult.SUCCESS)
                    viewModel.setCategoryFilter(categoryId ?: 0, this@MainActivity)
                fab.visibility = View.VISIBLE
            }
        })
        dialog.show(supportFragmentManager, "add_task_dialog")
        fab.visibility = View.GONE
    }

    private fun fabOnClick() {
        when (navHost.navController.currentDestination?.id) {
            R.id.allTaskFragment -> {
                showAddTaskDialog()
            }

            R.id.calendarFragment -> {
                navHost.navController.navigate(R.id.allTaskFragment)
                showAddTaskDialog()
            }
        }
    }
}