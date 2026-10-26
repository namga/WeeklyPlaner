package com.example.weeklylifeplanner

import android.os.Bundle
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val viTranslations = mapOf(
    "A little balance, every day ✨" to "Cân bằng mỗi ngày ✨", "Home" to "Trang chủ", "My week" to "Lịch của tôi", "Meals" to "Món ăn",
    "YOUR WEEK, YOUR WAY" to "TUẦN CỦA BẠN, THEO CÁCH BẠN MUỐN", "Make room for what matters." to "Dành thời gian cho điều quan trọng.",
    "Plan a balanced week with space to work, rest and grow." to "Lên kế hoạch cân bằng giữa công việc, nghỉ ngơi và phát triển bản thân.",
    "Generate a fresh week" to "Tạo lịch tuần mới", "💼 Work" to "💼 Công việc", "🌙 Sleep" to "🌙 Giấc ngủ", "☑️ Done" to "☑️ Hoàn thành",
    "Coming up this week" to "Lịch trình tuần này", "Week at a glance" to "Tổng quan tuần", "Time" to "Giờ", "Day agenda" to "Lịch theo ngày",
    "Week calendar" to "Lịch tuần", "Simple list of activities" to "Danh sách hoạt động đơn giản", "7-day time grid like Teams" to "Lịch 7 ngày dạng lưới như Teams",
    "Activity tracking" to "Theo dõi hoạt động", "Tick each activity when you finish it." to "Đánh dấu từng hoạt động khi hoàn thành.",
    "Show completion checkboxes" to "Hiện ô đánh dấu hoàn thành", "Make it yours ✨" to "Cá nhân hóa ✨", "Tune the look and feel of your planner." to "Tùy chỉnh giao diện lịch của bạn.",
    "Color theme" to "Màu giao diện", "Schedule display" to "Kiểu hiển thị lịch", "Your routine" to "Thói quen hằng ngày", "Work starts" to "Giờ bắt đầu làm việc",
    "Sleep starts" to "Giờ đi ngủ", "Sleep duration" to "Thời lượng ngủ", "Work on weekends" to "Làm việc cuối tuần", "Reset progress & refresh plan" to "Đặt lại tiến độ và tạo lịch mới",
    "Vietnamese meal ideas 🍲" to "Gợi ý món Việt 🍲", "Fresh suggestions for today. Tap refresh for another mix." to "Gợi ý cho hôm nay. Nhấn làm mới để đổi thực đơn.",
    "Suggest another menu" to "Gợi ý thực đơn khác", "Today's menu" to "Thực đơn hôm nay", "Breakfast" to "Bữa sáng", "Lunch" to "Bữa trưa", "Dinner" to "Bữa tối", "Snack" to "Bữa phụ",
    "Suggestions are randomized from common Vietnamese dishes; choose according to your taste, allergies and dietary needs." to "Món ăn được chọn ngẫu nhiên từ các món Việt phổ biến. Hãy cân nhắc khẩu vị, dị ứng và nhu cầu dinh dưỡng.",
    "Find recipes on Cookidoo ↗" to "Tìm công thức trên Cookidoo ↗", "Public recipe sources" to "Nguồn công thức công khai", "Some recipes may require sign-in or a subscription." to "Một số công thức có thể yêu cầu đăng nhập hoặc đăng ký.",
    "English" to "Tiếng Anh", "Fresh mint" to "Xanh bạc hà", "Sunny peach" to "Cam đào", "Sky blue" to "Xanh da trời", "Lavender" to "Tím oải hương", "Rose pink" to "Hồng phấn",
    "Balanced" to "Cân bằng", "Active" to "Năng động", "Relaxed" to "Thư thái", "7 hours" to "7 giờ", "8 hours" to "8 giờ", "9 hours" to "9 giờ",
    "Generate New Plan" to "Tạo lịch mới", "Work" to "Công việc", "Morning routine" to "Thói quen buổi sáng", "Coffee & reset" to "Cà phê và nghỉ ngắn",
    "Sport" to "Thể thao", "Personal time" to "Thời gian cá nhân", "Dinner" to "Bữa tối", "Study / learning" to "Học tập", "Movie & relax" to "Xem phim và thư giãn",
    "Free time" to "Thời gian tự do", "Wind down" to "Thư giãn trước khi ngủ", "Sleep" to "Ngủ", "Coffee & slow morning" to "Cà phê và buổi sáng thong thả",
    "Outdoor activity" to "Hoạt động ngoài trời", "Study" to "Học tập", "Shopping" to "Mua sắm", "Movie / outdoors" to "Xem phim / đi chơi", "Sport / walk" to "Thể thao / đi bộ",
    "Social / family" to "Bạn bè / gia đình", "Relax & personal time" to "Thư giãn và thời gian cá nhân", "Wake up & breakfast" to "Thức dậy và ăn sáng",
    "hours focused work" to "giờ làm việc tập trung", "💼 Work" to "💼 Công việc", "🌙 Sleep" to "🌙 Giấc ngủ", "☑️ Done" to "☑️ Hoàn thành", "Week at a glance" to "Tổng quan tuần", "Cân bằng mỗi ngày ✨" to "Cân bằng mỗi ngày ✨",
    "Monday" to "Thứ Hai", "Tuesday" to "Thứ Ba", "Wednesday" to "Thứ Tư", "Thursday" to "Thứ Năm", "Friday" to "Thứ Sáu", "Saturday" to "Thứ Bảy", "Sunday" to "Chủ nhật",
    "Mon" to "T2", "Tue" to "T3", "Wed" to "T4", "Thu" to "T5", "Fri" to "T6", "Sat" to "T7", "Sun" to "CN",
    "Lunch" to "Bữa trưa", "Wake up & breakfast" to "Thức dậy và ăn sáng", "Coffee & reset" to "Cà phê và nghỉ ngắn", "Wind down" to "Thư giãn trước khi ngủ",
    "Personal time" to "Thời gian cá nhân", "Study / learning" to "Học tập", "Movie & relax" to "Xem phim và thư giãn", "Free time" to "Thời gian tự do",
    "Coffee & slow morning" to "Cà phê và buổi sáng thong thả", "Movie / outdoors" to "Xem phim / đi chơi", "Sport / walk" to "Thể thao / đi bộ", "Social / family" to "Bạn bè / gia đình", "Relax & personal time" to "Thư giãn và thời gian cá nhân", "Month calendar" to "Lịch tháng", "This week" to "Tuần này", "Workday" to "Ngày làm việc", "Weekend" to "Cuối tuần",
    "Make it yours" to "Cá nhân hóa", "hours focused work" to "giờ làm việc tập trung", "Ngôn ngữ / Language" to "Ngôn ngữ", "8 hours focused work" to "8 giờ làm việc tập trung",
    "Work starts" to "Giờ bắt đầu làm việc", "Sleep starts" to "Giờ đi ngủ", "Sleep duration" to "Thời lượng ngủ", "Work on weekends" to "Làm việc cuối tuần",
    "Cân bằng mỗi ngày ✨" to "Cân bằng mỗi ngày ✨", "Bữa phụ" to "Bữa phụ", "Time" to "Giờ", "Generate a fresh week" to "Tạo lịch tuần mới"
)
private fun tr(language: String, text: String): String = if (language == "Tiếng Việt") viTranslations[text] ?: text else text

private data class ActivityBlock(val id:String, val start:Int, val end:Int, val name:String, val emoji:String)
private fun Int.clock() = "%02d:%02d".format((this/60)%24, this%60)
private val days = listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
private data class Prefs(val workStart:Int=480,val sleepStart:Int=1380,val sleepHours:Int=8,val weekendWork:Boolean=false,val lifestyle:String="Balanced",val theme:String="Fresh mint",val view:String="Day agenda",val tracking:Boolean=true,val language:String="English")
private val palettes = mapOf("Fresh mint" to Color(0xFF168C75), "Sunny peach" to Color(0xFFE87945), "Sky blue" to Color(0xFF3979D6), "Lavender" to Color(0xFF8061D9), "Rose pink" to Color(0xFFD84F83))
private fun plan(p:Prefs, seed:Int):List<List<ActivityBlock>> = days.mapIndexed { i, day ->
    val out= mutableListOf<ActivityBlock>(); fun add(s:Int,e:Int,n:String,em:String){if(s<e)out+=ActivityBlock("$i-$s-$n",s,e,n,em)}
    add(420,450,"Wake up & breakfast","☀️"); add(450,480,"Morning routine","🌿")
    if(i<5 || p.weekendWork){val w=p.workStart; add(w,w+240,"Work","💼");add(w+240,w+300,"Lunch","🍽️");add(w+300,w+540,"Work","💼");add(w+540,w+570,"Coffee & reset","☕");add(1020,1080,"Sport","🏃");add(1080,1140,"Personal time","💗");add(1140,1185,"Dinner","🍴");add(1185,1215,"Study / learning","📚");add(1215,1350,if((i+seed)%2==0)"Movie & relax" else "Free time",if((i+seed)%2==0)"🎬" else "💜");add(1350,p.sleepStart,"Wind down","🌙")}
    else {add(480,540,"Coffee & slow morning","☕");add(540,600,if(i==5)"Outdoor activity" else "Study",if(i==5)"🌲" else "📚");add(600,660,if(i==5)"Shopping" else "Outdoor activity",if(i==5)"🛍️" else "🌳");add(660,720,"Personal time","💗");add(720,780,"Lunch","🍽️");add(780,840,if(i==5)"Sport" else "Study",if(i==5)"🏃" else "📚");add(840,900,"Personal time","💗");add(900,960,"Movie / outdoors","🎬");add(960,1020,"Personal time","💗");add(1020,1080,"Sport / walk","🏃");add(1080,1140,"Free time","💜");add(1140,1200,"Dinner","🍴");add(1200,1260,"Social / family","👥");add(1260,1350,"Relax & personal time","💗");add(1350,p.sleepStart,"Wind down","🌙")}
    out+=ActivityBlock("$i-sleep-late",p.sleepStart,1440,"Sleep","🌙"); val end=(p.sleepStart+p.sleepHours*60)%1440; if(end>0)out.add(ActivityBlock("$i-sleep-early",0,end,"Sleep","🌙")); out.sortedBy{it.start}
}
class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{PlannerApp()}}}

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun PlannerApp(){
 val context = androidx.compose.ui.platform.LocalContext.current
 val storage = remember(context) { context.getSharedPreferences("weekly_life_planner", android.content.Context.MODE_PRIVATE) }; var tab by remember{mutableIntStateOf(0)}; var day by remember{mutableIntStateOf(0)}; var prefs by remember{mutableStateOf(Prefs(language=storage.getString("language","English")?:"English",theme=storage.getString("theme","Fresh mint")?:"Fresh mint",view=storage.getString("view","Day agenda")?:"Day agenda",tracking=storage.getBoolean("tracking",true),workStart=storage.getInt("workStart",480),sleepStart=storage.getInt("sleepStart",1380),sleepHours=storage.getInt("sleepHours",8),weekendWork=storage.getBoolean("weekendWork",false)))}; var seed by remember{mutableIntStateOf(0)}; val done= remember{ mutableStateListOf<String>().apply { addAll(storage.getStringSet("done", emptySet()) ?: emptySet()) } }; LaunchedEffect(prefs, done.toList()) { storage.edit().putString("language",prefs.language).putString("theme",prefs.theme).putString("view",prefs.view).putBoolean("tracking",prefs.tracking).putInt("workStart",prefs.workStart).putInt("sleepStart",prefs.sleepStart).putInt("sleepHours",prefs.sleepHours).putBoolean("weekendWork",prefs.weekendWork).putStringSet("done",done.toSet()).apply() }; val week=remember(prefs,seed){plan(prefs,seed)}; val accent=palettes[prefs.theme]?:palettes.getValue("Fresh mint")
 MaterialTheme(colorScheme=lightColorScheme(primary=accent,secondary=Color(0xFFFFB454),background=Color(0xFFF5F8FC),surface=Color.White)){
 Scaffold(containerColor=Color(0xFFF5F8FC),topBar={TopAppBar(title={Column{Text(tr(prefs.language, "Weekly Life Planner"),fontWeight=FontWeight.Bold);Text(tr(prefs.language,"A little balance, every day ✨"),fontSize=12.sp,color=Color.Gray)}},actions={IconButton(onClick={tab=3}){Icon(Icons.Default.Settings,null)}})},bottomBar={NavigationBar{NavigationBarItem(selected=tab==0,onClick={tab=0},icon={Icon(Icons.Default.Home,null)},label={Text(tr(prefs.language,"Home"))});NavigationBarItem(selected=tab==1,onClick={tab=1},icon={Icon(Icons.Default.DateRange,null)},label={Text(tr(prefs.language,"My week"))});NavigationBarItem(selected=tab==2,onClick={tab=2},icon={Icon(Icons.Default.RestaurantMenu,null)},label={Text(tr(prefs.language,"Meals"))})}}){pad->
 when(tab){
 0->LazyColumn(Modifier.padding(pad).fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
 item{Card(colors=CardDefaults.cardColors(containerColor=accent.copy(alpha=.12f)),shape=RoundedCornerShape(28.dp)){Column(Modifier.padding(22.dp)){Text(tr(prefs.language, "YOUR WEEK, YOUR WAY"),fontSize=11.sp,color=accent,fontWeight=FontWeight.Bold);Text(tr(prefs.language, "Make room for what matters."),fontSize=24.sp,fontWeight=FontWeight.Bold);Text(tr(prefs.language, "Plan a balanced week with space to work, rest and grow."),color=Color.DarkGray);Spacer(Modifier.height(16.dp));Button(onClick={seed++;day=0;tab=1},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(16.dp)){Icon(Icons.Default.AutoAwesome,null);Spacer(Modifier.width(8.dp));Text(tr(prefs.language, "Generate a fresh week"))}}}}
 item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){StatCard(tr(prefs.language,"💼 Work"),if(prefs.weekendWork)"56h" else "40h",Modifier.weight(1f),accent);StatCard(tr(prefs.language,"🌙 Sleep"),"${prefs.sleepHours*7}h",Modifier.weight(1f),accent);StatCard(tr(prefs.language,"☑️ Done"),"${done.size}",Modifier.weight(1f),accent)}}
 item{Text(tr(prefs.language, "Coming up this week"),fontSize=19.sp,fontWeight=FontWeight.Bold)}
 items(days.indices.toList()){i->Card(onClick={day=i;tab=1},shape=RoundedCornerShape(18.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){Row(Modifier.fillMaxWidth().padding(15.dp),verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(44.dp).background(accent.copy(alpha=.12f),RoundedCornerShape(14.dp)),contentAlignment=Alignment.Center){Text(listOf("🌱","☀️","🌼","🌿","✨","🌈","💗")[i],fontSize=22.sp)};Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(tr(prefs.language, days[i]),fontWeight=FontWeight.SemiBold);Text(week[i].filter{it.start>=420&&it.start<1380}.take(2).joinToString(" · "){tr(prefs.language,it.name)},fontSize=12.sp,color=Color.Gray)};Icon(Icons.Default.ChevronRight,null,tint=accent)}}}
 }
 1->Column(Modifier.padding(pad).fillMaxSize().padding(horizontal=14.dp)){
 if(prefs.view!="Week calendar") Row(Modifier.horizontalScroll(rememberScrollState()).padding(vertical=8.dp),horizontalArrangement=Arrangement.spacedBy(7.dp)){days.forEachIndexed{i,d->FilterChip(selected=day==i,onClick={day=i},label={Text(tr(prefs.language, d).take(3))})}}
 Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(if(prefs.view=="Week calendar")tr(prefs.language,"Week at a glance") else tr(prefs.language,days[day]),fontSize=22.sp,fontWeight=FontWeight.Bold);Text("${week[day].count{it.name=="Work"}*4} ${tr(prefs.language,"hours focused work")}",fontSize=12.sp,color=Color.Gray)};if(prefs.tracking)Text(if(prefs.language=="Tiếng Việt")"${week[day].count{it.id in done}}/${week[day].size} hoàn thành" else "${week[day].count{it.id in done}}/${week[day].size} done",color=accent,fontWeight=FontWeight.SemiBold)}
 if(prefs.view=="Week calendar"){
 val weekVerticalScroll = rememberScrollState()
 Row(Modifier.fillMaxWidth().weight(1f)) {
  // Keep the time rail fixed while the 24-hour grid scrolls vertically.
  Column(Modifier.width(54.dp)) {
   Text(tr(prefs.language, "Time"),Modifier.height(42.dp).padding(start=4.dp,top=8.dp),fontSize=10.sp,color=Color.Gray)
   Column(Modifier.fillMaxHeight().verticalScroll(weekVerticalScroll)) {
    (0..23).forEach { h -> Box(Modifier.height(58.dp).fillMaxWidth(),contentAlignment=Alignment.TopStart) { Text("%02d:00".format(h),Modifier.padding(top=5.dp,start=2.dp),fontSize=10.sp,color=Color.Gray) } }
   }
  }
  Column(Modifier.weight(1f).horizontalScroll(rememberScrollState())) {
   Row { days.forEachIndexed { index, d ->
    val dayLabel = if (prefs.language == "Tiếng Việt") listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")[index] else listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")[index]
    Text(dayLabel,Modifier.width(112.dp).height(42.dp).padding(6.dp),fontSize=11.sp,fontWeight=FontWeight.Bold,color=accent)
   } }
   Column(Modifier.fillMaxHeight().verticalScroll(weekVerticalScroll)) {
    (0..23).forEach { h -> Row(Modifier.height(58.dp),verticalAlignment=Alignment.Top) { days.indices.forEach { i -> val event=week[i].firstOrNull{it.start/60==h}; Column(Modifier.width(112.dp).fillMaxHeight().padding(end=3.dp,bottom=3.dp).background(Color(0xFFE9EDF4),RoundedCornerShape(6.dp)).padding(3.dp)) { if(event!=null) Text(tr(prefs.language, "${event.emoji} ${event.name}"),fontSize=10.sp,color=accent,maxLines=3) } } } }
   }
  }
 }
 }else if(prefs.view=="Month calendar"){
 Column(verticalArrangement=Arrangement.spacedBy(6.dp)) { Row { listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun").forEach { Text(it,Modifier.weight(1f),fontSize=10.sp,fontWeight=FontWeight.Bold,color=accent) } }; (1..35).chunked(7).forEachIndexed { row, nums -> Row(horizontalArrangement=Arrangement.spacedBy(4.dp)) { nums.forEachIndexed { col, n -> val idx=(row*7+col)%7; Card(onClick={ day = idx }, modifier=Modifier.weight(1f).height(76.dp), shape=RoundedCornerShape(10.dp), colors=CardDefaults.cardColors(containerColor=if(n==day+1)accent.copy(alpha=.15f) else Color.White)) { Column(Modifier.padding(5.dp)) { Text(if(n<=31)n.toString() else "",fontSize=11.sp,fontWeight=FontWeight.Bold); if(n<=31)Text(week[idx].firstOrNull{it.start>=420&&it.start<1380}?.emoji?:"",fontSize=15.sp); if(n<=31&&prefs.tracking)Text(tr(prefs.language, "${week[idx].count{it.id in done} }✓"),fontSize=9.sp,color=accent) } } } } } }
 }else{LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp),contentPadding=PaddingValues(bottom=12.dp)){items(week[day]){event->val checked=event.id in done;Card(shape=RoundedCornerShape(17.dp),colors=CardDefaults.cardColors(containerColor=if(checked)accent.copy(alpha=.08f) else Color.White)){Row(Modifier.fillMaxWidth().padding(horizontal=10.dp,vertical=8.dp),verticalAlignment=Alignment.CenterVertically){if(prefs.tracking)Checkbox(checked=checked,onCheckedChange={if(it){if(event.id !in done)done.add(event.id)}else done.remove(event.id)},colors=CheckboxDefaults.colors(checkedColor=accent));Box(Modifier.size(40.dp).background(accent.copy(alpha=.10f),RoundedCornerShape(13.dp)),contentAlignment=Alignment.Center){Text(event.emoji,fontSize=21.sp)};Spacer(Modifier.width(10.dp));Column(Modifier.weight(1f)){Text(tr(prefs.language,event.name),fontWeight=FontWeight.SemiBold,color=if(checked)Color.Gray else Color(0xFF233047));Text(tr(prefs.language, "${event.start.clock()} – ${event.end.clock()}"),fontSize=12.sp,color=Color.Gray)};if(checked)Icon(Icons.Default.CheckCircle,null,tint=accent)}}}}}
 }
 2->LazyColumn(Modifier.padding(pad).fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
 item { Text(tr(prefs.language, "Vietnamese meal ideas 🍲"), fontSize=24.sp, fontWeight=FontWeight.Bold); Text(tr(prefs.language, "Fresh suggestions for today. Tap refresh for another mix."), color=Color.Gray) }
 item { Button(onClick={seed++}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(16.dp)) { Icon(Icons.Default.Refresh,null); Spacer(Modifier.width(8.dp)); Text(tr(prefs.language, "Suggest another menu")) } }
 val meals = listOf(
  Triple("Breakfast", "🥖", listOf("Bánh mì trứng", "Phở bò", "Bún riêu", "Xôi gà", "Bánh cuốn", "Cháo thịt bằm", "Hủ tiếu")),
  Triple("Lunch", "🍚", listOf("Cơm gà xối mỡ", "Cá kho tộ + rau luộc", "Thịt kho trứng + canh", "Bún thịt nướng", "Cơm sườn nướng", "Đậu hũ sốt cà + canh", "Gà kho gừng")),
  Triple("Dinner", "🍲", listOf("Canh chua cá + cơm", "Bún bò Huế", "Gỏi cuốn + súp", "Miến gà", "Cá hấp gừng + rau", "Cháo sườn", "Bún chả")),
  Triple("Snack", "🍉", listOf("Sữa chua trái cây", "Bắp luộc", "Khoai lang hấp", "Thanh long", "Chuối + sữa", "Đậu nành", "Ổi chấm muối ớt"))
 )
 val picked = meals.mapIndexed { idx, m -> m.first to m.third[(seed * 3 + idx * 2 + 1) % m.third.size] }
 item { Text(tr(prefs.language, "Today's menu"), fontSize=18.sp, fontWeight=FontWeight.Bold) }
 items(picked) { item -> Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=accent.copy(alpha=.09f))) { Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment=Alignment.CenterVertically) { Text(meals.first{it.first==item.first}.second, fontSize=30.sp); Spacer(Modifier.width(14.dp)); Column(Modifier.weight(1f)) { Text(tr(prefs.language,item.first), color=accent, fontWeight=FontWeight.Bold); Text(item.second, fontSize=17.sp, fontWeight=FontWeight.SemiBold); TextButton(onClick={ val query=when(item.first){"Breakfast"->"bữa sáng";"Lunch"->"bữa trưa";"Dinner"->"bữa tối";else->"món ăn nhẹ"}; context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://cookidoo.international/search/vi?q="+java.net.URLEncoder.encode(query,"UTF-8")))) }) { Text(tr(prefs.language,"Find recipes on Cookidoo ↗"), color=accent) } } } } }
 item { Text(tr(prefs.language, "Public recipe sources"), fontSize=18.sp, fontWeight=FontWeight.Bold) }
 item { Card(shape=RoundedCornerShape(18.dp)) { Column(Modifier.padding(14.dp)) { Text("Cookidoo Việt Nam", fontWeight=FontWeight.SemiBold); TextButton(onClick={context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://cookidoo.thermomix.vn/foundation/vi-VN/explore")))}) { Text(tr(prefs.language,"Find recipes on Cookidoo ↗"), color=accent) }; Text("Cookidoo – tìm kiếm công thức", fontWeight=FontWeight.SemiBold); TextButton(onClick={context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://cookidoo.international/search/vi")))}) { Text(tr(prefs.language,"Find recipes on Cookidoo ↗"), color=accent) }; Text(tr(prefs.language,"Some recipes may require sign-in or a subscription."), fontSize=12.sp, color=Color.Gray) } } }
 item { Text(tr(prefs.language, "Suggestions are randomized from common Vietnamese dishes; choose according to your taste, allergies and dietary needs."), fontSize=12.sp, color=Color.Gray) }
 }
 else->LazyColumn(Modifier.padding(pad).fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){
 item{Text(tr(prefs.language, "Make it yours ✨"),fontSize=24.sp,fontWeight=FontWeight.Bold);Text(tr(prefs.language, "Tune the look and feel of your planner."),color=Color.Gray)}
 item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(16.dp)){Text(tr(prefs.language, "Color theme"),fontWeight=FontWeight.Bold);Spacer(Modifier.height(10.dp));palettes.forEach{(name,color)->Row(Modifier.fillMaxWidth().padding(vertical=3.dp),verticalAlignment=Alignment.CenterVertically){RadioButton(selected=prefs.theme==name,onClick={prefs=prefs.copy(theme=name)});Box(Modifier.size(24.dp).background(color,RoundedCornerShape(8.dp)));Text(tr(prefs.language,name),Modifier.weight(1f).padding(start=10.dp));if(prefs.theme==name)Icon(Icons.Default.Check,null,tint=color)}}}}}
 item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(16.dp)){Text(tr(prefs.language, "Schedule display"),fontWeight=FontWeight.Bold);listOf("Day agenda","Week calendar").forEach{v->Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){RadioButton(selected=prefs.view==v,onClick={prefs=prefs.copy(view=v)});Column{Text(tr(prefs.language,v),fontWeight=FontWeight.Medium);Text(tr(prefs.language,if(v=="Day agenda")"Simple list of activities" else "7-day time grid like Teams"),fontSize=12.sp,color=Color.Gray)}}}}}}
 item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(16.dp)){Text(tr(prefs.language, "Activity tracking"),fontWeight=FontWeight.Bold);Text(tr(prefs.language, "Tick each activity when you finish it."),fontSize=12.sp,color=Color.Gray);Row(verticalAlignment=Alignment.CenterVertically){Text(tr(prefs.language, "Show completion checkboxes"),Modifier.weight(1f));Switch(checked=prefs.tracking,onCheckedChange={prefs=prefs.copy(tracking=it)})}}}}
 item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){Text(if(prefs.language=="Tiếng Việt")"Ngôn ngữ" else "Language",fontWeight=FontWeight.Bold);SettingString(if(prefs.language=="Tiếng Việt")"Ngôn ngữ" else "Language",listOf("English","Tiếng Việt"),prefs.language){prefs=prefs.copy(language=it)}}}}
 item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){Text(tr(prefs.language, "Your routine"),fontWeight=FontWeight.Bold);SettingSelect(tr(prefs.language,"Work starts"),listOf("07:00" to 420,"08:00" to 480,"09:00" to 540),prefs.workStart, prefs.language){prefs=prefs.copy(workStart=it)};SettingSelect(tr(prefs.language,"Sleep starts"),listOf("22:00" to 1320,"23:00" to 1380,"23:30" to 1410),prefs.sleepStart, prefs.language){prefs=prefs.copy(sleepStart=it)};SettingSelect(tr(prefs.language,"Sleep duration"),listOf("7 hours" to 7,"8 hours" to 8,"9 hours" to 9),prefs.sleepHours, prefs.language){prefs=prefs.copy(sleepHours=it)};Row(verticalAlignment=Alignment.CenterVertically){Text(tr(prefs.language, "Work on weekends"),Modifier.weight(1f));Switch(checked=prefs.weekendWork,onCheckedChange={prefs=prefs.copy(weekendWork=it)})}}}}
 item{Button(onClick={done.clear();seed++;day=0},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(16.dp)){Icon(Icons.Default.Refresh,null);Spacer(Modifier.width(8.dp));Text(tr(prefs.language, "Reset progress & refresh plan"))}}
 }
 }
 }}
}
@Composable private fun StatCard(label:String,value:String,modifier:Modifier,accent:Color){Card(modifier=modifier,shape=RoundedCornerShape(18.dp)){Column(Modifier.padding(12.dp)){Text(label,fontSize=12.sp);Text(value,fontSize=20.sp,fontWeight=FontWeight.Bold,color=accent)}}}
@Composable private fun SettingSelect(title:String,options:List<Pair<String,Int>>,current:Int,language:String="English",onSelect:(Int)->Unit){var expanded by remember{mutableStateOf(false)};Row(verticalAlignment=Alignment.CenterVertically){Text(title,Modifier.weight(1f));Box{TextButton(onClick={expanded=true}){Text(tr(language,options.firstOrNull{it.second==current}?.first?:current.toString()));Icon(Icons.Default.ArrowDropDown,null)};DropdownMenu(expanded=expanded,onDismissRequest={expanded=false}){options.forEach{(label,value)->DropdownMenuItem(text={Text(tr(language,label))},onClick={ onSelect(value); expanded=false })}}}}}

@Composable private fun SettingString(title:String,options:List<String>,current:String,onSelect:(String)->Unit){var expanded by remember{mutableStateOf(false)};Row(verticalAlignment=Alignment.CenterVertically){Text(title,Modifier.weight(1f));Box{TextButton(onClick={expanded=true}){Text(current);Icon(Icons.Default.ArrowDropDown,null)};DropdownMenu(expanded=expanded,onDismissRequest={expanded=false}){options.forEach{value->DropdownMenuItem(text={Text(value)},onClick={onSelect(value);expanded=false})}}}}}
