const DAYS = ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
const shortDays = ["Mon","Tue","Wed","Thu","Fri","Sat","Sun"];
const defaults = {workStart:480,sleepStart:1380,sleepHours:8,weekendWork:"off",lifestyle:"Balanced",language:"en"};
let prefs = {...defaults}, selectedDay = 0, seed = 0;
const $ = id => document.getElementById(id);
const clock = n => `${String(Math.floor(n/60)%24).padStart(2,"0")}:${String(n%60).padStart(2,"0")}`;
function add(list,start,end,name,emoji){if(start<end)list.push({start,end,name,emoji});}
function makeDay(i){
  const p=prefs, list=[], workday=i<5||p.weekendWork==="on", ws=Number(p.workStart);
  add(list,420,450,"Wake up & breakfast","☀️"); add(list,450,480,"Morning routine","🌿");
  if(workday){
    add(list,ws,ws+240,"Work","💼"); add(list,ws+240,ws+300,"Lunch","🍽️"); add(list,ws+300,ws+540,"Work","💼");
    add(list,ws+540,ws+570,"Coffee & reset","☕");
    add(list,ws+570,ws+630,"Sport","🏃"); add(list,ws+630,ws+690,"Personal time","💗");
    add(list,ws+690,ws+735,"Dinner","🍴"); add(list,ws+735,ws+765,"Study / learning","📚");
    add(list,ws+765,22*60+30, (i+seed)%2===0?"Movie & relax":"Free time",(i+seed)%2===0?"🎬":"💜");
    add(list,22*60+30,Number(p.sleepStart),"Wind down","🌙");
  }else{
    add(list,480,540,"Coffee & slow morning","☕");
    add(list,540,600,i===5?"Outdoor activity":"Study",i===5?"🌲":"📚");
    add(list,600,660,i===5?"Shopping":"Outdoor activity",i===5?"🛍️":"🌳");
    add(list,660,720,"Personal time","💗"); add(list,720,780,"Lunch","🍽️");
    add(list,780,840,i===5?"Sport":"Study",i===5?"🏃":"📚");
    add(list,840,900,i===5?"Personal time":"Shopping",i===5?"💗":"🛍️");
    add(list,900,960,i===5?"Movie":"Outdoor activity",i===5?"🎬":"🌳");
    add(list,960,1020,"Personal time","💗"); add(list,1020,1080,"Sport / walk","🏃");
    add(list,1080,1140,"Free time","💜"); add(list,1140,1200,"Dinner","🍴");
    add(list,1200,1260,i===5?(seed%2===0?"Movie night":"Social / family"):(seed%2===0?"Social / family":"Movie night"),i===5?(seed%2===0?"🎬":"👥"):(seed%2===0?"👥":"🎬"));
    add(list,1260,1350,"Relax & personal time","💗"); add(list,1350,Number(p.sleepStart),"Wind down","🌙");
  }
  // Sleep interval may cross midnight; render as two segments so there are no overlaps.
  const ss=Number(p.sleepStart), duration=Number(p.sleepHours)*60, end=(ss+duration)%1440;
  list.push({start:ss,end:1440,name:"Sleep",emoji:"🌙"});
  if(end>0) list.push({start:0,end,name:"Sleep",emoji:"🌙"});
  list.sort((a,b)=>a.start-b.start);
  return list.filter(x=>x.end>x.start);
}
function render(){
  $("workStat").textContent=(prefs.weekendWork==="on"?56:40)+"h";
  $("sleepStat").textContent=(Number(prefs.sleepHours)*7)+"h";
  $("lifeStat").textContent=prefs.lifestyle;
  $("dayPicker").innerHTML=DAYS.map((d,i)=>`<button class="day-button ${i===selectedDay?"active":""}" data-day="${i}">${shortDays[i]}<small>${i<5?"Workday":"Weekend"}</small></button>`).join("");
  $("dayPicker").querySelectorAll("button").forEach(b=>b.addEventListener("click",()=>{selectedDay=Number(b.dataset.day);render();}));
  $("dayTitle").textContent=DAYS[selectedDay];
  const workday=selectedDay<5||prefs.weekendWork==="on";
  $("dayMeta").textContent=workday?"8 hours work":"Weekend · no work";
  const list=makeDay(selectedDay);
  $("timeline").innerHTML=list.map(e=>`<article class="event"><time>${clock(e.start)}–${clock(e.end===1440?0:e.end)}</time><span class="emoji">${e.emoji}</span><div><strong>${e.name}</strong><small>${Math.round((e.end-e.start)/6)/10} hours</small></div></article>`).join("");
}
function saveForm(){
  prefs={workStart:Number($("workStart").value),sleepStart:Number($("sleepStart").value),sleepHours:Number($("sleepHours").value),weekendWork:$("weekendWork").value,lifestyle:$("lifestyle").value,language:$("language").value};
  document.documentElement.lang=prefs.language; render();
}
$("settingsForm").addEventListener("submit",e=>{e.preventDefault();saveForm();document.querySelector('[data-tab="week"]').click();});
$("generate").addEventListener("click",()=>{seed++;selectedDay=0;render();});
document.querySelectorAll(".tab").forEach(btn=>btn.addEventListener("click",()=>{document.querySelectorAll(".tab").forEach(b=>b.classList.toggle("active",b===btn));$("weekView").hidden=btn.dataset.tab!=="week";$("settingsView").hidden=btn.dataset.tab!=="settings";}));
if("serviceWorker" in navigator && location.protocol.startsWith("http")) navigator.serviceWorker.register("./sw.js").catch(()=>{});
render();
