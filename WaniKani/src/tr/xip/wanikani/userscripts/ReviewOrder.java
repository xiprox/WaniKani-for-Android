package tr.xip.wanikani.userscripts;


/*
 * A port of the alucardeck's WK Reorder script (https://www.wanikani.com/chat/api-and-third-party-apps/3023)
 * The script can be downloaded from here: http://userscripts.org/scripts/show/174704
 * I had to replace events with direct function calls because they don't seem to work on all android devices
 */
public class ReviewOrder {

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
                    "    \r\n" +
                    "    t.innerHTML = '<div id=\"wkroStatus\"><table align=\"right\"><tbody>'+\r\n" +
                    "        '<tr><td>Rad</td><td align=\"right\"><span id=\"wkroRadCount\"></span></td></tr>'+\r\n" +
                    "        '<tr><td>Kan</td><td align=\"right\"><span id=\"wkroKanCount\"></span></td></tr>'+\r\n" +
                    "        '<tr><td>Voc</td><td align=\"right\"><span id=\"wkroVocCount\"></span></td></tr>'+\r\n" +
                    "        '</tbody></table></div><div id=\"divSt\">Not Ordered!</div>'+\r\n" +
                    "        '<button id=\"reorderBtn1\" type=\"button\" onclick=\"window.wknReorderBulk();\">Reorder</button>'+\r\n" +
//"        '<button id=\"reorderBtn2\" type=\"button\" onclick=\"window.wknReorderSingle();\">Single Mode</button>'+\r\n" +
                    "        '</div>';\r\n" +
                    "    $.jStorage.listenKeyChange(\"activeQueue\",displayUpdate);\r\n" +
//"	window.addEventListener('reorderWKSingle',reorderSingle); \r\n" +
//"	window.addEventListener('reorderWKBulk',reorderBulk); \r\n" +
                    "    displayUpdate();\r\n" +
                    "	console.log('init() end');\r\n" +
                    "}\r\n" +
                    "\r\n" +
                    "window.wknReorderBulk = function() {\r\n" +
                    "    //Reordering method following original parameters of 10 activeQueue list\r\n" +
                    "    method = \"BULK\";\r\n" +
                    "    reorder();\r\n" +
                    "}\r\n" +
                    "\r\n" +
                    "window.wknReorderSingle = function() {\r\n" +
                    "    //Reordering method following the 1 activeQueue list, that makes both reading/meaning coming in pairs.\r\n" +
                    "    method = \"SINGLE\";\r\n" +
                    "    reorder();\r\n" +
                    "}\r\n" +
                    "\r\n" +
                    "function reorder(){\r\n" +
                    "    console.log('reorder() start');\r\n" +
                    "    var divSt = get(\"divSt\");\r\n" +
                    "    reorderBtn1.style.visibility=\"hidden\";\r\n" +
//"    reorderBtn2.style.visibility=\"hidden\";\r\n" +
                    "    divSt.innerHTML = 'Reordering.. please wait!';\r\n" +
                    "        \r\n" +
                    "    var cur = $.jStorage.get(\"currentItem\");\r\n" +
                    "	var qt = $.jStorage.get(\"questionType\");\r\n" +
                    "	var actList = $.jStorage.get(\"activeQueue\");\r\n" +
                    "	var revList = $.jStorage.get(\"reviewQueue\");\r\n" +
                    "    \r\n" +
                    "    console.log('current item: '+cur);\r\n" +
                    "    var curt = cur.kan?'kan':cur.voc?'voc':'rad';\r\n" +
                    "    \r\n" +
                    "    var removedCount = 0;\r\n" +
                    "    for(var i=0;i<actList.length;i++){\r\n" +
                    "        var it = actList[i];\r\n" +
                    "        var itt = cur.kan?'kan':cur.voc?'voc':'rad';\r\n" +
                    "        console.log(it);\r\n" +
                    "        if(!(curt==itt&&cur.id==it.id)){\r\n" +
                    "           actList.splice(i--,1);\r\n" +
                    "           revList.push(it);\r\n" +
                    "           removedCount++;\r\n" +
                    "        }\r\n" +
                    "    }\r\n" +
                    "    console.log('Items removed from ActiveQueue: '+removedCount);\r\n" +
                    "    \r\n" +
                    "    for(var i=revList.length-1;i>=0;i--){\r\n" +
                    "        var it=revList[i];\r\n" +
                    "        if(it.kan){\r\n" +
                    "           revList.splice(i,1);\r\n" +
                    "           revList.push(it);\r\n" +
                    "           //console.log('kan '+it.kan);\r\n" +
                    "        }\r\n" +
                    "    }\r\n" +
                    "    for(var i=revList.length-1;i>=0;i--){\r\n" +
                    "        var it=revList[i];\r\n" +
                    "        if(it.rad){\r\n" +
                    "           revList.splice(i,1);\r\n" +
                    "           revList.push(it);\r\n" +
                    "           //console.log('rad '+it.rad);\r\n" +
                    "        }\r\n" +
                    "    }\r\n" +
                    "    \r\n" +
                    "    if(method=='SINGLE')\r\n" +
                    "        Math.random = function() { return 0;  };" +   // @Ikalou's fix
                    "    for(var i=0;i<removedCount;i++)\r\n" +
                    "       	actList.push(revList.pop());\r\n" +
                    "    \r\n" +
                    "    console.log('Ordered ReviewQueue:');\r\n" +
                    "    for(var i=0;i<revList.length;i++){\r\n" +
                    "        var it=revList[i];\r\n" +
                    "        if(it.rad)\r\n" +
                    "        	console.log('rad '+it.rad);\r\n" +
                    "        else if(it.kan)\r\n" +
                    "           	console.log('kan '+it.kan);\r\n" +
                    "        else if(it.voc)\r\n" +
                    "            console.log('voc '+it.voc);\r\n" +
                    "    }\r\n" +
                    "    \r\n" +
                    "    $.jStorage.set(\"reviewQueue\",revList);\r\n" +
                    "    $.jStorage.set(\"activeQueue\",actList);\r\n" +
                    "    \r\n" +
                    "    divSt.innerHTML = 'Done!';\r\n" +
                    "    console.log('reorder() end');\r\n" +
                    "}\r\n" +
                    "\r\n" +
                    "function displayUpdate(){\r\n" +
                    "    var radC = 0, kanC = 0, vocC = 0;\r\n" +
                    "    var list = $.jStorage.get(\"reviewQueue\").concat($.jStorage.get(\"activeQueue\"));\r\n" +
                    "    console.log('ReviewQueue ('+$.jStorage.get(\"reviewQueue\").length+') ActiveQueue ('+$.jStorage.get(\"activeQueue\").length+')');\r\n" +
                    "    for(var i=0;i<list.length;i++){\r\n" +
                    "        var it=list[i];\r\n" +
                    "        if(it.rad)\r\n" +
                    "        	radC++;\r\n" +
                    "        else if(it.kan)\r\n" +
                    "           	kanC++;\r\n" +
                    "        else if(it.voc)\r\n" +
                    "           	vocC++;\r\n" +
                    "    }\r\n" +
                    "    console.log('Rad '+radC+' Kan '+kanC+' Voc '+vocC);\r\n" +
                    "    var radSpan = $(\"#wkroRadCount\")[0];\r\n" +
                    "    var kanSpan = $(\"#wkroKanCount\")[0];\r\n" +
                    "    var vocSpan = $(\"#wkroVocCount\")[0];\r\n" +
                    "    radSpan.innerHTML = radC;\r\n" +
                    "    kanSpan.innerHTML = kanC;\r\n" +
                    "    vocSpan.innerHTML = vocC;\r\n" +
                    "}\r\n" +
                    "var method = \"\";\r\n" +

// Glue code //
                    "if ($(\"#wkroStatus\").length > 0) {" +
                    "    $(\"#wkroStatus\").show (); " +
                    "    $(\"#divSt\").show (); " +
                    "    $(\"#reorderBtn1\").show (); " +
//"    $(\"#reorderBtn2\").show (); " +
                    "} else {" +
                    "    init();\r\n" +
                    "    console.log('script load end');" +
                    "}";

    public static final String JS_UNINIT_CODE =
            "$(\"#wkroStatus\").hide ();" +
                    "$(\"#divSt\").hide (); " +
                    "$(\"#reorderBtn1\").hide ();" +
//"$(\"#reorderBtn2\").hide ();" +
                    "";
}