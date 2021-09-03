package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.content.ContentValues
import android.os.Bundle
import android.provider.UserDictionary
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kidmobi.R
import com.kidmobi.databinding.FragmentDeviceManagementWebTabBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DeviceManagementWebTabFragment : Fragment() {
    private lateinit var binding: FragmentDeviceManagementWebTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management_web_tab, container, false)
        return binding.root
    }

    private fun getBrowserHistory() {
        val resolver = requireActivity().contentResolver
        val projection = arrayOf(
            UserDictionary.Words._ID,
            UserDictionary.Words.LOCALE,
            UserDictionary.Words.WORD,
            UserDictionary.Words.FREQUENCY,
            UserDictionary.Words.APP_ID
        )

        val sortOrder = UserDictionary.Words.LOCALE
        val cursor = resolver.query(UserDictionary.Words.CONTENT_URI, projection, null, null, sortOrder)


        cursor.let { cr ->
            val data = StringBuffer()

            println("****************************************")
            if (cr != null) {
                while (cr.moveToNext()) {
                    data.append(cr.getString(1) + "," + cr.getString(2) + ", " + cr.getString(3))
                    data.append("\n")
                    println(data)
                }
            }
            println("****************************************")
        }
        cursor?.close()
    }

    private fun getList2() {
        println("getList2() is started!")

        CoroutineScope(Dispatchers.Default + Job()).launch {
            val cv = ContentValues()
            cv.put(UserDictionary.Words.APP_ID, "com.kidmobi")
            cv.put(UserDictionary.Words.WORD, "istikbal halı")
            cv.put(UserDictionary.Words.LOCALE, "tr")
            cv.put(UserDictionary.Words.FREQUENCY, "1000")


            cv.put(UserDictionary.Words.APP_ID, "com.kidmobi")
            cv.put(UserDictionary.Words.WORD, "shaggy halı")
            cv.put(UserDictionary.Words.LOCALE, "tr")
            cv.put(UserDictionary.Words.FREQUENCY, "1000")

            cv.put(UserDictionary.Words.APP_ID, "com.kidmobi")
            cv.put(UserDictionary.Words.WORD, "tuğra halı")
            cv.put(UserDictionary.Words.LOCALE, "tr")
            cv.put(UserDictionary.Words.FREQUENCY, "1000")

            requireContext().contentResolver.insert(UserDictionary.Words.CONTENT_URI, cv)


            val cursor = requireContext().contentResolver.query(
                UserDictionary.Words.CONTENT_URI, null, null, null, null
            )!!

            try {
                val wordColumn = cursor.getColumnIndex(UserDictionary.Words.WORD)
                println("Cursor is not null")
                while (cursor.moveToNext()) {
                    val word = cursor.getString(wordColumn)
                    println("Cursor is trying to get word!")
                    println(word)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                println("Cursor is null or process finished")
                cursor.close()
            }
        }
    }
}