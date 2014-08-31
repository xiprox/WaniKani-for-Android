package tr.xip.wanikani.userscripts;

/*
 * A port of the alucardeck's WK Reorder script (https://www.wanikani.com/chat/api-and-third-party-apps/3878)
 * The script can be downloaded from here: http://userscripts.org/scripts/review/182793
 * I had to replace events with direct function calls because they don't seem to work on all android devices
 */
public class LessonOrder {

    public static final String JS_CODE =
            "function get(id) {\r\n" +
                    "    if (id && typeof id === 'string') {\r\n" +
                    "        id = document.getElementById(id);\r\n" +
                    "    }\r\n" +
                    "    return id || null;\r\n" +
                    "}\r\n" +
                    "\r\n" +
                    "function init(){\r\n" +
                    "	console.log('init() start');\r\n" +
                    "	var stats = $(\"#stats\")[0];\r\n" +
                    "    var t = document.createElement('div');\r\n" +
                    "    stats.appendChild(t);\r\n" +
                    "    t.innerHTML = '<div id=\"divSt\"><div>Not Ordered!</div><button id=\"reorderBtn\" type=\"button\" onclick=\"window.wknReorder();\">Reorder!</button></div>';\r\n" +
//"    window.addEventListener('reorderWK',reorder); \r\n" +
                    "    console.log('init() end');\r\n" +
                    "}\r\n" +
                    "\r\n" +
//"function reorder(){\r\n" +
                    "window.wknReorder = function() {\r\n" +
                    "    console.log('reorder() start');\r\n" +
                    "    var divSt = get(\"divSt\");\r\n" +
                    "    var reorderBtn = get(\"reorderBtn\");\r\n" +
                    "    reorderBtn.style.visibility=\"hidden\";\r\n" +
                    "    divSt.innerHTML = 'Reordering.. please wait!';\r\n" +
                    "	var actList = $.jStorage.get(\"l/activeQueue\");\r\n" +
                    "	var lesList = $.jStorage.get(\"l/lessonQueue\");\r\n" +
                    "    \r\n" +
                    "    var removedCount = 0;\r\n" +
                    "    for(var i=1;i<actList.length;i++){\r\n" +
                    "        var it = actList[i];\r\n" +
                    "        actList.splice(i--,1);\r\n" +
                    "        lesList.push(it);\r\n" +
                    "        removedCount++;\r\n" +
                    "    }\r\n" +
                    "    console.log('Items removed from ActiveQueue: '+removedCount);\r\n" +
                    "    \r\n" +
                    "    for(var i=lesList.length-1;i>=0;i--){\r\n" +
                    "        var it=lesList[i];\r\n" +
                    "        if(it.kan){\r\n" +
                    "           lesList.splice(i,1);\r\n" +
                    "           lesList.push(it);\r\n" +
                    "        }\r\n" +
                    "    }\r\n" +
                    "    for(var i=lesList.length-1;i>=0;i--){\r\n" +
                    "        var it=lesList[i];\r\n" +
                    "        if(it.rad){\r\n" +
                    "           lesList.splice(i,1);\r\n" +
                    "           lesList.push(it);\r\n" +
                    "        }\r\n" +
                    "    }\r\n" +
                    "    \r\n" +
                    "    for(var i=0;i<removedCount;i++){\r\n" +
                    "        actList.push(lesList.pop());\r\n" +
                    "    }\r\n" +
                    "        \r\n" +
                    "    console.log('Ordered LessonQueue:');\r\n" +
                    "    for(var i=0;i<lesList.length;i++){\r\n" +
                    "        var it=lesList[i];\r\n" +
                    "        if(it.rad)\r\n" +
                    "        	console.log('rad '+it.rad);\r\n" +
                    "        else if(it.kan)\r\n" +
                    "           	console.log('kan '+it.kan);\r\n" +
                    "        else if(it.voc)\r\n" +
                    "            console.log('voc '+it.voc);\r\n" +
                    "    }\r\n" +
                    "    \r\n" +
                    "    $.jStorage.set(\"l/lessonQueue\",lesList);\r\n" +
                    "    $.jStorage.set(\"l/activeQueue\",actList);\r\n" +
                    "    divSt.innerHTML = 'Done!';\r\n" +
                    "    console.log('reorder() end');\r\n" +
                    "}\r\n" +
                    "\r\n" +
// Glue code //
                    "if ($(\"#divSt\").length > 0) {" +
                    "    $(\"#divSt\").show (); " +
                    "} else {" +
                    "    init();\r\n" +
                    "    console.log('script load end');" +
                    "}";

    public static final String JS_REFRESH_CODE =
            "    var divSt = document.getElementById (\"divSt\");\r\n" +
                    "    divSt.innerHTML = '<div>Not Ordered!</div><button id=\"reorderBtn\" type=\"button\" onclick=\"window.wknReorder();\">Reorder!</button>'";

    public static final String JS_UNINIT_CODE =
            "$(\"#divSt\").hide ();";
}