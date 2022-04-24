package com.example.otpreader

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class OtpListViewModel : ViewModel() {

    companion object {
        const val TAG = "projectlog"
    }

    private val _smsMessages = MutableLiveData<List<String>?>()
    val smsMessages: LiveData<List<String>?>
        get() = _smsMessages

    fun getSmsList(context: Context) {
        viewModelScope.launch {
            val smsList = mutableListOf<String>()

            val contentResolver = context.contentResolver ?: return@launch
            val cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
                ?: return@launch

            val indexAddress = cursor.getColumnIndex("address");
            val indexBody = cursor.getColumnIndex("body")

            if (indexBody < 0 || !cursor.moveToFirst()) return@launch

            do {
                val str = cursor.getString(indexBody)
                smsList.add(str)
            } while (cursor.moveToNext())


            _smsMessages.value = smsList
        }
    }
}