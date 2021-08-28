package com.kidmobi.business.observers

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper

class WebHistoryObserver(val resolver: ContentResolver) : ContentObserver(Handler(Looper.getMainLooper())) {

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)

        val cr: Cursor
        var sb: StringBuilder

        val BOOKMARKS_URI: Uri = Uri.parse("content://com.android.chrome.browser/bookmarks")
        val projection = arrayOf(
            "_id",  // 0
            "url",  // 1
            "visits",  // 2
            "date",  // 3
            "bookmark",  // 4
            "title",  // 5
            "favicon",  // 6
            "thumbnail",  // 7
            "touch_icon",  // 8
            "user_entered"
        )

        cr = resolver.query(BOOKMARKS_URI, projection, null, null, null)!!
        cr.moveToFirst()
        println("WebHistoryObserver is started!!!!!!!!!!!!!!!")
        println(cr)
        var title = ""
        var date = ""
        var visits = ""
        var url = ""
        var info = ""
        if (cr.moveToFirst() && cr.count > 0) {
            while (!cr.isAfterLast) {
                title = cr.getString(5)
                date = cr.getString(3)
                url = cr.getString(1)
                visits = cr.getString(2)
                info = "$title date: $date url: $url visits$visits\n"
                println(info)
                cr.moveToNext()
            }
        }

        cr.close()
    }
}