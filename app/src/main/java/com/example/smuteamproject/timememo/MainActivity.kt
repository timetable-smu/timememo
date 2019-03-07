package kr.ac.smu.cs.twoparkoneko_2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private val FORMATTER = SimpleDateFormat("yyyy-MM-dd")

    private var dateFormat: String? = null
    private var inputDate: Date? = null

    //Activity간 RequestCode
    private val REGISTER_ACTIVITY = 2
    private val SHOW_ALL_LIST_ACTIVITY = 3


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode) {

            Activity.RESULT_OK -> when(requestCode) {
                SHOW_ALL_LIST_ACTIVITY -> {
                    intent.getIntExtra("data", 0)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        var materialCalendar = findViewById<MaterialCalendarView>(R.id.mainMaCalendarView)
        var todayDate: Date = Date()




        //Today Date를 달력 상단에 띄운다.
        mainTextViewToday.text = "Today :: ${FORMATTER.format(todayDate)}"



        //보여줄 달력의 범위를 지정(2018-12-1 ~ 2020-12-31)
        materialCalendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2018, 0,1))
            .setMaximumDate(CalendarDay.from(2020, 11,31))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()



        /*
           달력에 효과를 주기
           -오늘 날짜 -> 핑크색
           -일요일    -> 빨간색
           -토요일    -> 파란색
         */
        materialCalendar.addDecorators(OneDayDecorator(), SaturdayDecorator(), SundayDecorator())



        //클릭 이벤트 처리
        materialCalendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {

                //date()는 사용자가 선택한 날짜를 리턴한다. getDate()이고 return은 Date형이다.
                inputDate = date.date
                dateFormat = FORMATTER.format(inputDate)

                Toast.makeText(this@MainActivity, dateFormat , Toast.LENGTH_SHORT).show()
            }
        })


        //달력에서 날짜를 선택한 후에 '선택한 해당 날짜'를 RegisterActivity.kt로 넘긴다.
        //FloatingButtonEvent 처리
        fab.setOnClickListener { view ->

            //날짜 선택을 안했을 시 예외처리
            if(inputDate == null) {
                Toast.makeText(this, "날짜를 선택하세요~", Toast.LENGTH_LONG).show()
            }
            else {
                var intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra("InputDate", dateFormat)
                startActivityForResult(intent, REGISTER_ACTIVITY)
            }

        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )


        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // navigationView에서 MenuItem Click시 반응.
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.toShowAllList -> {

                var intent = Intent(this, ShowAllListActivity::class.java)
                intent.putExtra("from",100)

//                intent.putExtra("InputDate", dateFormat)
                startActivityForResult(intent, SHOW_ALL_LIST_ACTIVITY)
            }

            //갤러리 이동
            /*
            // R.id.toGallery -> {

            }
            */
            //공유하기
            R.id.nav_share -> {

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}