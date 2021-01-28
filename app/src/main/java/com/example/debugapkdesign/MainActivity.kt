package com.example.debugapkdesign


import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_footer_main.*


class MainActivity : AppCompatActivity(), FolderFragment.SendData {
    var fragmentManager : FragmentManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        fragmentManager = supportFragmentManager
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState();
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment, FolderFragment())
        ft.commit()
        navigationListner()
        drawer_layout.closeDrawers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    fun navigationListner() {
        LLFolderview.setOnClickListener {
            setFtagment(FolderFragment())
        }
        LLFileview.setOnClickListener {
            setFtagment(FileFragment())
        }
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.fragment)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        }
        else if(f!!.javaClass.name.equals(FolderFragment::class.java.name))
        {
            finish()
        }
        else
        {
            setFtagment(FolderFragment())
        }
    }

    fun setFtagment(fragment: Fragment) {
        if (fragment != null) {
            val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
            ft.replace(R.id.fragment, fragment)
            ft.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun setData(path: String) {
        var fragment = FileFragment.newInstance(1)
        var bundle = Bundle()
        if (fragment != null) {
            bundle.putString("path", path)
            fragment.arguments = bundle
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment, fragment)
            ft.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }

}