package com.elderlylauncher

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class AppListActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)

        val listView: ListView = findViewById(R.id.appListView)
        val pm: PackageManager = packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        val appNames = apps.map { pm.getApplicationLabel(it).toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, appNames)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val appName = appNames[position]
            val appInfo = apps.find { pm.getApplicationLabel(it) == appName }
            appInfo?.let {
                val launchIntent = pm.getLaunchIntentForPackage(it.packageName)
                launchIntent?.let { startActivity(it) }
            }
        }
    }
}
