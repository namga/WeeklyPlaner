package com.example.weeklylifeplanner

import android.os.Bundle
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

private data class ActivityBlock(val id:String, val start:Int, val end:Int, val name:String, val emoji:String)
private fun Int.clock() = "%02d:%02d".format((this/60)%24, this%60)
private val days = listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
private data class Prefs(val workStart:Int=480,val sleepStart:Int=1380,val sleepHours:Int=8,val weekendWork:Boolean=false,val lifestyle:String="Balanced",val theme:String="Fresh mint",val view:String="Day agenda",val tracking:Boolean=true)
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
 var tab by remember{mutableIntStateOf(0)}; var day by remember{mutableIntStateOf(0)}; var prefs by remember{mutableStateOf(Prefs())}; var seed by remember{mutableIntStateOf(0)}; val done= remember{ mutableStateListOf<String>() }; val week=remember(prefs,seed){plan(prefs,seed)}; val accent=palettes[prefs.theme]?:palettes.getValue("Fresh mint")
 MaterialTheme(colorScheme=lightColorScheme(primary=accent,secondary=Color(0xFFFFB454),background=Color(0xFFF5F8FC),surface=Color.White)){
 Scaffold(containerColor=Color(0xFFF5F8FC),topBar={TopAppBar(title={Column{Text("Weekly Life Planner",fontWeight=FontWeight.Bold);Text("A little balance, every day ✨",fontSize=12.sp,color=Color.Gray)}},actions={IconButton(onClick={tab=2}){Icon(Icons.Default.Settings,null)}})},bottomBar={NavigationBar{NavigationBarItem(selected=tab==0,onClick={tab=0},icon={Icon(Icons.Default.Home,null)},label={Text("Home")});NavigationBarItem(selected=tab==1,onClick={tab=1},icon={Icon(Icons.Default.DateRange,null)},label={Text("My week")});NavigationBarItem(selected=tab==2,onClick={tab=2},icon={Icon(Icons.Default.Palette,null)},label={Text("Settings")})}}){pad->
 when(tab){
 0->LazyColumn(Modifier.padding(pad).fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
 item{Card(colors=CardDefaults.cardColors(containerColor=accent.copy(alpha=.12f)),shape=RoundedCornerShape(28.dp)){Column(Modifier.padding(22.dp)){Text("YOUR WEEK, YOUR WAY",fontSize=11.sp,color=accent,fontWeight=FontWeight.Bold);Text("Make room for what matters.",fontSize=24.sp,fontWeight=FontWeight.Bold);Text("Plan a balanced week with space to work, rest and grow.",color=Color.DarkGray);Spacer(Modifier.height(16.dp));Button(onClick={seed++;day=0;tab=1},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(16.dp)){Icon(Icons.Default.AutoAwesome,null);Spacer(Modifier.width(8.dp));Text("Generate a fresh week")}}}}
 item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){StatCard("💼 Work",if(prefs.weekendWork)"56h" else "40h",Modifier.weight(1f),accent);StatCard("🌙 Sleep","${prefs.sleepHours*7}h",Modifier.weight(1f),accent);StatCard("☑️ Done","${done.size}",Modifier.weight(1f),accent)}}
 item{Text("Coming up this week",fontSize=19.sp,fontWeight=FontWeight.Bold)}
 items(days.indices.toList()){i->Card(onClick={day=i;tab=1},shape=RoundedCornerShape(18.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){Row(Modifier.fillMaxWidth().padding(15.dp),verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(44.dp).background(accent.copy(alpha=.12f),RoundedCornerShape(14.dp)),contentAlignment=Alignment.Center){Text(listOf("🌱","☀️","🌼","🌿","✨","🌈","💗")[i],fontSize=22.sp)};Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(days[i],fontWeight=FontWeight.SemiBold);Text(week[i].filter{it.start>=420&&it.start<1380}.take(2).joinToString(" · "){it.name},fontSize=12.sp,color=Color.Gray)};Icon(Icons.Default.ChevronRight,null,tint=accent)}}}
 }
 1->Column(Modifier.padding(pad).fillMaxSize().padding(horizontal=14.dp)){
 if(prefs.view!="Week calendar") Row(Modifier.horizontalScroll(rememberScrollState()).padding(vertical=8.dp),horizontalArrangement=Arrangement.spacedBy(7.dp)){days.forEachIndexed{i,d->FilterChip(selected=day==i,onClick={day=i},label={Text(d.take(3))})}}
 Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(if(prefs.view=="Week calendar")"Week at a glance" else days[day],fontSize=22.sp,fontWeight=FontWeight.Bold);Text("${week[day].count{it.name=="Work"}*4} hours focused work",fontSize=12.sp,color=Color.Gray)};if(prefs.tracking)Text("${week[day].count{it.id in done}}/${week[day].size} done",color=accent,fontWeight=FontWeight.SemiBold)}
 if(prefs.view=="Week calendar"){
 Row(Modifier.fillMaxWidth()) {
  // Keep the time rail outside the horizontal scroller so it remains visible while swiping across days.
  Column(Modifier.width(54.dp)) {
   Text("Time",Modifier.height(42.dp).padding(start=4.dp,top=8.dp),fontSize=10.sp,color=Color.Gray)
   (7..22).forEach { h -> Box(Modifier.height(58.dp).fillMaxWidth(),contentAlignment=Alignment.TopStart) { Text("%02d:00".format(h),Modifier.padding(top=5.dp,start=2.dp),fontSize=10.sp,color=Color.Gray) } }
  }
  Column(Modifier.weight(1f).horizontalScroll(rememberScrollState())) {
   Row { days.forEach { d -> Text(d.take(3),Modifier.width(112.dp).height(42.dp).padding(6.dp),fontSize=11.sp,fontWeight=FontWeight.Bold,color=accent) } }
   (7..22).forEach { h -> Row(Modifier.height(58.dp),verticalAlignment=Alignment.Top) { days.indices.forEach { i -> val event=week[i].firstOrNull{it.start/60==h}; Column(Modifier.width(112.dp).fillMaxHeight().padding(end=3.dp,bottom=3.dp).background(Color(0xFFE9EDF4),RoundedCornerShape(6.dp)).padding(3.dp)) { if(event!=null) Text("${event.emoji} ${event.name}",fontSize=10.sp,color=accent,maxLines=3) } } } }
  }
 }
 }else if(prefs.view=="Month calendar"){
 Column(verticalArrangement=Arrangement.spacedBy(6.dp)) { Row { listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun").forEach { Text(it,Modifier.weight(1f),fontSize=10.sp,fontWeight=FontWeight.Bold,color=accent) } }; (1..35).chunked(7).forEachIndexed { row, nums -> Row(horizontalArrangement=Arrangement.spacedBy(4.dp)) { nums.forEachIndexed { col, n -> val idx=(row*7+col)%7; Card(onClick={ day = idx }, modifier=Modifier.weight(1f).height(76.dp), shape=RoundedCornerShape(10.dp), colors=CardDefaults.cardColors(containerColor=if(n==day+1)accent.copy(alpha=.15f) else Color.White)) { Column(Modifier.padding(5.dp)) { Text(if(n<=31)n.toString() else "",fontSize=11.sp,fontWeight=FontWeight.Bold); if(n<=31)Text(week[idx].firstOrNull{it.start>=420&&it.start<1380}?.emoji?:"",fontSize=15.sp); if(n<=31&&prefs.tracking)Text("${week[idx].count{it.id in done} }✓",fontSize=9.sp,color=accent) } } } } } }
 }else{LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp),contentPadding=PaddingValues(bottom=12.dp)){items(week[day]){event->val checked=event.id in done;Card(shape=RoundedCornerShape(17.dp),colors=CardDefaults.cardColors(containerColor=if(checked)accent.copy(alpha=.08f) else Color.White)){Row(Modifier.fillMaxWidth().padding(horizontal=10.dp,vertical=8.dp),verticalAlignment=Alignment.CenterVertically){if(prefs.tracking)Checkbox(checked=checked,onCheckedChange={if(it){if(event.id !in done)done.add(event.id)}else done.remove(event.id)},colors=CheckboxDefaults.colors(checkedColor=accent));Box(Modifier.size(40.dp).background(accent.copy(alpha=.10f),RoundedCornerShape(13.dp)),contentAlignment=Alignment.Center){Text(event.emoji,fontSize=21.sp)};Spacer(Modifier.width(10.dp));Column(Modifier.weight(1f)){Text(event.name,fontWeight=FontWeight.SemiBold,color=if(checked)Color.Gray else Color(0xFF233047));Text("${event.start.clock()} – ${event.end.clock()}",fontSize=12.sp,color=Color.Gray)};if(checked)Icon(Icons.Default.CheckCircle,null,tint=accent)}}}}}
 }
 else->LazyColumn(Modifier.padding(pad).fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){
 item{Text("Make it yours ✨",fontSize=24.sp,fontWeight=FontWeight.Bold);Text("Tune the look and feel of your planner.",color=Color.Gray)}
 item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(16.dp)){Text("Color theme",fontWeight=FontWeight.Bold);Spacer(Modifier.height(10.dp));palettes.forEach{(name,color)->Row(Modifier.fillMaxWidth().padding(vertical=3.dp),verticalAlignment=Alignment.CenterVertically){RadioButton(selected=prefs.theme==name,onClick={prefs=prefs.copy(theme=name)});Box(Modifier.size(24.dp).background(color,RoundedCornerShape(8.dp)));Text(name,Modifier.weight(1f).padding(start=10.dp));if(prefs.theme==name)Icon(Icons.Default.Check,null,tint=color)}}}}}
 item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(16.dp)){Text("Schedule display",fontWeight=FontWeight.Bold);listOf("Day agenda","Week calendar").forEach{v->Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){RadioButton(selected=prefs.view==v,onClick={prefs=prefs.copy(view=v)});Column{Text(v,fontWeight=FontWeight.Medium);Text(when(v){"Day agenda"->"Simple list of activities";"Week calendar"->"7-day time grid like Teams";else->"Simple list of activities"},fontSize=12.sp,color=Color.Gray)}}}}}}
 item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(16.dp)){Text("Activity tracking",fontWeight=FontWeight.Bold);Text("Tick each activity when you finish it.",fontSize=12.sp,color=Color.Gray);Row(verticalAlignment=Alignment.CenterVertically){Text("Show completion checkboxes",Modifier.weight(1f));Switch(checked=prefs.tracking,onCheckedChange={prefs=prefs.copy(tracking=it)})}}}}
 item{Card(shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){Text("Your routine",fontWeight=FontWeight.Bold);SettingSelect("Work starts",listOf("07:00" to 420,"08:00" to 480,"09:00" to 540),prefs.workStart){prefs=prefs.copy(workStart=it)};SettingSelect("Sleep starts",listOf("22:00" to 1320,"23:00" to 1380,"23:30" to 1410),prefs.sleepStart){prefs=prefs.copy(sleepStart=it)};SettingSelect("Sleep duration",listOf("7 hours" to 7,"8 hours" to 8,"9 hours" to 9),prefs.sleepHours){prefs=prefs.copy(sleepHours=it)};Row(verticalAlignment=Alignment.CenterVertically){Text("Work on weekends",Modifier.weight(1f));Switch(checked=prefs.weekendWork,onCheckedChange={prefs=prefs.copy(weekendWork=it)})}}}}
 item{Button(onClick={done.clear();seed++;day=0},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(16.dp)){Icon(Icons.Default.Refresh,null);Spacer(Modifier.width(8.dp));Text("Reset progress & refresh plan")}}
 }
 }
 }}
}
@Composable private fun StatCard(label:String,value:String,modifier:Modifier,accent:Color){Card(modifier=modifier,shape=RoundedCornerShape(18.dp)){Column(Modifier.padding(12.dp)){Text(label,fontSize=12.sp);Text(value,fontSize=20.sp,fontWeight=FontWeight.Bold,color=accent)}}}
@Composable private fun SettingSelect(title:String,options:List<Pair<String,Int>>,current:Int,onSelect:(Int)->Unit){var expanded by remember{mutableStateOf(false)};Row(verticalAlignment=Alignment.CenterVertically){Text(title,Modifier.weight(1f));Box{TextButton(onClick={expanded=true}){Text(options.firstOrNull{it.second==current}?.first?:current.toString());Icon(Icons.Default.ArrowDropDown,null)};DropdownMenu(expanded=expanded,onDismissRequest={expanded=false}){options.forEach{(label,value)->DropdownMenuItem(text={Text(label)},onClick={ onSelect(value); expanded=false })}}}}}
