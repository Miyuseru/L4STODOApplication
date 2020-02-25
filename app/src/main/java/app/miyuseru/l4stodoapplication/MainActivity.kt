package app.miyuseru.l4stodoapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*
import java.nio.file.Files.delete
import java.util.*


class MainActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        val taskList = readAll()





//        CheckBox()
//
//        val checkBox = findViewById<CheckBox>(R.id.checkbox)
//        checkBox.setOnCheckedChangeListener {checkBox, isChecked ->
//            startActivity(Intent(applicationContext,Active::class.java))
//        }
//
//

        //画面遷移するよ
        plusButton.setOnClickListener {

            startActivity(Intent(this, CreateActivity::class.java))



        }



        val adapter = //TaskAdapter(this, taskList, true)
        TaskAdapter(this,taskList, object : TaskAdapter.OnItemClickListener {
            override fun onItemClick(item: Task) {


                val preview = Intent(applicationContext,detail::class.java)
                preview.putExtra("Title" , item.Todo)
                preview.putExtra("create" , item.content)

                preview.putExtra("id" , item.id)

                startActivity(preview)


            }
        },true)



        val checkBox =
            TaskAdapter(this, taskList, object : TaskAdapter.CheckBoxClickListener {
                override fun onClick(item: CheckBox) {
                    // クリック時の処理
                    Toast.makeText(applicationContext, item.content + "を削除しました", Toast.LENGTH_SHORT).show()

                }
            }, true)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val message = when (item?.itemId) {
            R.id.all -> {
                resources.getString(R.string.all_add)
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }



                R.id.active -> {resources.getString(R.string.active_add)
                startActivity(Intent(applicationContext, Active::class.java))
            }


                R.id.completed -> {
                    resources.getString(R.string.completed_add)
                    startActivity(Intent(applicationContext, Conpleted::class.java))
                }
              else -> super.onOptionsItemSelected(item)

        }

        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    //だみ−10個作るよ
//     fun createData() {
//
//            create(R.drawable.ic_launcher_background, "あああ")
//
//    }

    fun create(todo: String, content: String) {
        realm.executeTransaction {
            val task = it.createObject(Task::class.java, UUID.randomUUID().toString())
            task.Todo = todo
            task.content = content
        }
    }

    fun readAll(): RealmResults<Task> {
        return realm.where(Task::class.java).findAll().sort("createdAt", Sort.ASCENDING)
    }



//  fun CheckBox(){
//
//      // set the listener upon the checkbox
//      checkBox.setOnClickListener(
//          View.OnClickListener
//          {
//              val check = checkBox.isChecked()
//              if (check ) {
//                  startActivity(Intent(applicationContext,Active::class.java))
//              } else {
//                  startActivity(Intent(applicationContext,Conpleted::class.java))
//          }
//
// })
  }
}





