package aeb.proyecto.habittracker.data.repo

import aeb.proyecto.habittracker.data.dao.NotificationDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepo @Inject constructor(
    private val notificationDao: NotificationDao
){
}