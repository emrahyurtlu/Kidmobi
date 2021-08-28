package com.kidmobi.business.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class WebHistoryProvider : ContentProvider() {

    var cursor: Cursor? = null

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

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun onCreate(): Boolean {
        TODO("Implement this to initialize your content provider on startup.")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        cursor = context?.contentResolver?.query(uri, projection, selection, selectionArgs, sortOrder)
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}