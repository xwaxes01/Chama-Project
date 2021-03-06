package  dev.ronnie.chama.groups

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import dev.ronnie.chama.R
import dev.ronnie.chama.admin.AdminActivity
import dev.ronnie.chama.cashandinvestments.CashAndInvestmentsActivity
import dev.ronnie.chama.chat.ChatRoomActivity
import dev.ronnie.chama.dashBoard.GroupDashBoard
import dev.ronnie.chama.databinding.ActivityMainGroupBinding
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.projects.ProjectsActivity
import kotlinx.android.synthetic.main.activity_main_group.*


class MainGroupActivity : AppCompatActivity(), MainGroupListener {

    lateinit var viewModel: MainGroupViewModel
    lateinit var binding: ActivityMainGroupBinding
    lateinit var group: Groups
    private lateinit var intentComing: Intent


    companion object {
        var isUserAdmin: Boolean = false


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main_group)
        viewModel = ViewModelProviders.of(this)[MainGroupViewModel::class.java]
        binding.mainGroupModel = viewModel
        viewModel.listener = this


        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Chama"

            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }

        }
        getPendingIntent()


    }

    private fun setOnclick(group: Groups) {
        cardMembers.setOnClickListener {
            val intent = Intent(this, MembersActivity::class.java)
            intent.putExtra("group", group)
            startActivity(intent)
        }
        cardChat.setOnClickListener {
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("group", group)
            startActivity(intent)
        }


    }

    override fun openActivities(code: Int) {

        when (code) {
            1 -> {
                val intent = Intent(this, GroupDashBoard::class.java)
                intent.putExtra("group", group)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, CashAndInvestmentsActivity::class.java)
                intent.putExtra("group", group)
                startActivity(intent)
            }
            3 -> {
                val intent = Intent(this, ProjectsActivity::class.java)
                intent.putExtra("group", group)
                startActivity(intent)
            }

        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        intentComing = intent

        if (intentComing.hasExtra("pending_intent_group")) {
            group = intentComing.getParcelableExtra("pending_intent_group")
            setOnclick(group)
        } else if (intentComing.hasExtra("group")) {
            group = intentComing.getParcelableExtra("group")
            setOnclick(group)
        }

        val adminCode = group.creator_id


        if (adminCode == FirebaseAuth.getInstance().currentUser!!.uid) {

            isUserAdmin = true
        }

        val inflater = menuInflater
        inflater.inflate(R.menu.admin_menu, menu)
        if (isUserAdmin) {
            menu.findItem(R.id.admin).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.admin) {
            val intent = Intent(this, AdminActivity::class.java)
            intent.putExtra("group", group)
            startActivity(intent)

        }
        return super.onOptionsItemSelected(item)
    }


    private fun getPendingIntent() {
        val intent = intent
        if (intent.hasExtra("pending_intent_group")) {

            val groupPending: Groups = intent.getParcelableExtra("pending_intent_group")

            ChatRoomActivity.mMessageIdSet?.clear()
            ChatRoomActivity.mMessagesList?.clear()

            ChatRoomActivity.mAdapterChat?.notifyDataSetChanged()

            val chatroomIntent = Intent(this, ChatRoomActivity::class.java)
            chatroomIntent.putExtra("group", groupPending)
            startActivity(chatroomIntent)

        }
    }

    override fun onStop() {
        super.onStop()
        isUserAdmin = false

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
