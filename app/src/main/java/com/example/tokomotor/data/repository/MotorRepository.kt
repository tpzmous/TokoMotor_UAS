package com.example.tokomotor.data.repository

import android.content.Context
import com.example.tokomotor.data.db.MotorDao
import com.example.tokomotor.data.model.Motor

class MotorRepository(context: Context) {
    private val dao = MotorDao(context)

    fun getAll() = dao.getAll()
    fun getById(id: Int) = dao.getById(id)
    fun insert(m: Motor) = dao.insert(m)
    fun update(m: Motor) = dao.update(m)
    fun delete(id: Int) = dao.delete(id)
}