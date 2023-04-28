import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences("TasksApp.sharedprefs", MODE_PRIVATE)
    }

    var accessToken: String?
        get() = Key.ACCESS_TOKEN.getString()
        set(value) = Key.ACCESS_TOKEN.setString(value)

    var taskList: String?
        get() = Key.TASK_LIST.getString()
        set(value) = Key.TASK_LIST.setString(value)


    private enum class Key {
        ACCESS_TOKEN,TASK_LIST;

        fun getString(): String? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(name, "") else null
        fun setString(value: String?) = value?.let { sharedPreferences!!.edit { putString(name, value) } } ?: remove()
        fun exists(): Boolean = sharedPreferences!!.contains(name)
        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}