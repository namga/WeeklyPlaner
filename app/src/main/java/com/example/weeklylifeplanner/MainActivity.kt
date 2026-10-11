package com.example.weeklylifeplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

private data class ActivityBlock(val start: Int, val end: Int, val name: String, val emoji: String) {
    fun time(): String = "${start.toClock()}–${end.toClock()}"
}
private fun Int.toClock() = "%02d:%02d".format(this / 60, this % 60)
private data class DayPlan(val day: String, val blocks: List<ActivityBlock>)
private data class Preferences(
    val workStart: Int = 8 * 60,
    val sleepStart: Int = 23 * 60,
    val sleepHours: Int = 8,
    val weekendWork: Boolean = false,
    val lifestyle: String = "Balanced",
    val language: String = "en"
)

private val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

private fun generateWeek(p: Preferences, seed: Int = 0): List<DayPlan> {
    val sleepStart = p.sleepStart
    val sleepEnd = (sleepStart + p.sleepHours * 60) % 1440
    val workDays = if (p.weekendWork) 0..6 else 0..4
    return dayNames.mapIndexed { index, day ->
        val blocks = mutableListOf<ActivityBlock>()
        fun add(start: Int, end: Int, name: String, emoji: String) {
            if (start < end) blocks += ActivityBlock(start, end, name, emoji)
        }
        // Daytime schedule is 07:00–23:00 for the requested default sleep window.
        add(7*60, 7*60+30, "Wake up & breakfast", "☀️")
        add(7*60+30, 8*60, "Morning routine", "🌿")
        if (index in workDays) {
            val ws = p.workStart
            add(ws, ws+4*60, "Work", "💼")
            add(ws+4*60, ws+5*60, "Lunch", "🍽️")
            add(ws+5*60, ws+9*60, "Work", "💼")
            add(ws+9*60, ws+9*60+30, "Coffee & reset", "☕")
        } else {
            add(8*60, 9*60, "Coffee & slow morning", "☕")
            add(9*60, 10*60, if (index == 5) "Outdoor activity" else "Study", if (index == 5) "🌲" else "📚")
            add(10*60, 11*60, if (index == 5) "Shopping" else "Outdoor activity", if (index == 5) "🛍️" else "🌳")
            add(11*60, 12*60, "Personal time", "💗")
            add(12*60, 13*60, "Lunch", "🍽️")
        }
        if (index in workDays) {
            add(17*60+30, 18*60+30, "Sport", "🏃")
            add(18*60+30, 19*60, "Personal time", "💗")
            add(19*60, 19*60+45, "Dinner", "🍴")
            add(19*60+45, 20*60+15, "Study / learning", "📚")
            add(20*60+15, 22*60+30, if ((index + seed) % 2 == 0) "Movie & relax" else "Free time", if ((index + seed) % 2 == 0) "🎬" else "💜")
            add(22*60+30, 23*60, "Wind down", "🌙")
        } else {
            add(13*60, 14*60, if (index == 5) "Sport" else "Study", if (index == 5) "🏃" else "📚")
            add(14*60, 15*60, if (index == 5) "Personal time" else "Shopping", if (index == 5) "💗" else "🛍️")
            add(15*60, 16*60, if (index == 5) "Movie" else "Outdoor activity", if (index == 5) "🎬" else "🌳")
            add(16*60, 17*60, "Personal time", "💗")
            add(17*60, 18*60, "Sport / walk", "🏃")
            add(18*60, 19*60, "Free time", "💜")
            add(19*60, 20*60, "Dinner", "🍴")
            add(20*60, 21*60, if (index == 5) "Movie night" else "Social / family", if (index == 5) "🎬" else "👥")
            add(21*60, 22*60+30, "Relax & personal time", "💗")
            add(22*60+30, 23*60, "Wind down", "🌙")
        }
        // Keep schedule blocks sorted; sleep is represented as an overnight block on each day.
        val daytime = blocks.sortedBy { it.start }.toMutableList()
        daytime += ActivityBlock(sleepStart, 1440, "Sleep", "🌙")
        if (sleepEnd > 0) daytime.add(0, ActivityBlock(0, sleepEnd, "Sleep", "🌙"))
        DayPlan(day, daytime)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PlannerApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlannerApp() {
    var tab by remember { mutableStateOf(0) }
    var selectedDay by remember { mutableIntStateOf(0) }
    var planSeed by remember { mutableIntStateOf(0) }
    var prefs by remember { mutableStateOf(Preferences()) }
    val week = remember(prefs, planSeed) {generateWeek(prefs, planSeed)}
    val blue = Color(0xFF3F55F5)
    MaterialTheme(colorScheme = lightColorScheme(primary = blue, secondary = Color(0xFF25B6E8))) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Column {
                        Text("Weekly Life Planner", fontWeight = FontWeight.Bold)
                        Text("Randomize your week, balance your life", fontSize = 12.sp, color = Color.Gray)
                    } },
                    actions = { IconButton(onClick = { tab = 2 }) { Icon(Icons.Default.Settings, "Settings") } }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(selected = tab == 0, onClick = { tab = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
                    NavigationBarItem(selected = tab == 1, onClick = { tab = 1 }, icon = { Icon(Icons.Default.DateRange, null) }, label = { Text("Weekly") })
                    NavigationBarItem(selected = tab == 2, onClick = { tab = 2 }, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Settings") })
                }
            }
        ) { padding ->
            when (tab) {
                0 -> LazyColumn(Modifier.padding(padding).fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE7F6FF)), shape = RoundedCornerShape(24.dp)) {
                            Column(Modifier.padding(20.dp)) {
                                Text("Good morning!", fontSize = 23.sp, fontWeight = FontWeight.Bold)
                                Text("Let's make this week amazing!")
                                Spacer(Modifier.height(16.dp))
                                Button(onClick = { planSeed++; selectedDay = 0; tab = 1 }, modifier = Modifier.fillMaxWidth()) {
                                    Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(8.dp)); Text("Generate New Plan")
                                }
                            }
                        }
                    }
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SummaryCard("Work", if (prefs.weekendWork) "56h" else "40h", Modifier.weight(1f))
                            SummaryCard("Sleep", "${prefs.sleepHours * 7}h", Modifier.weight(1f))
                            SummaryCard("Free time", "Balanced", Modifier.weight(1f))
                        }
                    }
                    item { Text("This week", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    items(week) { plan ->
                        Card(onClick = { selectedDay = dayNames.indexOf(plan.day); tab = 1 }) {
                            Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(plan.day, fontWeight = FontWeight.SemiBold)
                                    Text(plan.blocks.filter { it.start >= 7*60 && it.start < 23*60 }.take(3).joinToString(" · ") { it.name }, fontSize = 12.sp, color = Color.Gray)
                                }
                                Icon(Icons.Default.ChevronRight, null)
                            }
                        }
                    }
                }
                1 -> Column(Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp)) {
                    Text("Weekly schedule", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        dayNames.forEachIndexed { i, name ->
                            FilterChip(selected = selectedDay == i, onClick = { selectedDay = i }, label = { Text(name.take(3)) })
                        }
                    }
                    Text(dayNames[selectedDay], fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(week[selectedDay].blocks) { block ->
                            Row(Modifier.fillMaxWidth().background(Color(0xFFF3F6FC), RoundedCornerShape(12.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(block.emoji, fontSize = 22.sp)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(block.name, fontWeight = FontWeight.Medium)
                                    Text(block.time(), fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
                else -> LazyColumn(Modifier.padding(padding).fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item { Text("Preferences", fontSize = 22.sp, fontWeight = FontWeight.Bold) }
                    item {
                        PreferenceRow("Work start", prefs.workStart.toClock()) {
                            prefs = prefs.copy(workStart = if (prefs.workStart == 8*60) 9*60 else 8*60)
                        }
                    }
                    item {
                        PreferenceRow("Sleep start", prefs.sleepStart.toClock()) {
                            prefs = prefs.copy(sleepStart = if (prefs.sleepStart == 23*60) 22*60 else 23*60)
                        }
                    }
                    item {
                        PreferenceRow("Sleep duration", "${prefs.sleepHours} hours") {
                            prefs = prefs.copy(sleepHours = if (prefs.sleepHours == 8) 7 else 8)
                        }
                    }
                    item {
                        PreferenceRow("Weekend work", if (prefs.weekendWork) "On" else "Off") {
                            prefs = prefs.copy(weekendWork = !prefs.weekendWork)
                        }
                    }
                    item {
                        PreferenceRow("Preferred lifestyle", prefs.lifestyle) {
                            prefs = prefs.copy(lifestyle = if (prefs.lifestyle == "Balanced") "Active" else "Balanced")
                        }
                    }
                    item { PreferenceRow("Language", prefs.language) { prefs = prefs.copy(language = if (prefs.language == "en") "vi" else "en") } }
                    item {
                        Text("Tap a row to cycle its setting. The week regenerates automatically; default is English, balanced, weekdays only.", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F5FF))) {
        Column(Modifier.padding(12.dp)) {
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PreferenceRow(title: String, value: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(title, Modifier.weight(1f))
            Text(value, color = Color(0xFF3F55F5), fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}
