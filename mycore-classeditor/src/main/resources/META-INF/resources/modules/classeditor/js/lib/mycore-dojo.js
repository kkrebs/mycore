//>>built
require({cache:{"mycore/dijit/dijit-all":function(){define("./AbstractDialog ./PlainButton ./ExceptionDialog ./I18nRow ./Preloader ./Repeater ./RepeaterRow ./SimpleDialog ./TextRow".split(" "),function(){console.warn("dijit-all may include much more code than your application actually requires. We strongly recommend that you investigate a custom build or the web build tool");return{}})},"mycore/dijit/PlainButton":function(){define("dojo/_base/declare dijit/form/Button dojo/text!./templates/PlainButton.html dojo/on dojo/_base/lang dojo/dom-construct dojo/dom-class dojo/dom-style".split(" "),
function(e,k,h,a,d,b,g,f){return e("mycore.dijit.PlainButton",[k],{templateString:h,baseClass:"plainButton",constructor:function(c){e.safeMixin(this,c)},_setDisabledAttr:function(c){this.inherited(arguments);c?g.add(this.iconNode,"plain-button-disabled"):g.remove(this.iconNode,"plain-button-disabled")}})})},"mycore/common/common-all":function(){define("./CompoundEdit ./EventDelegator ./I18nStore ./I18nResolver ./I18nManager ./Preloader ./UndoableEdit ./UndoableMergeEdit ./UndoManager".split(" "),
function(){console.warn("common-all may include much more code than your application actually requires. We strongly recommend that you investigate a custom build or the web build tool");return{}})},"mycore/common/Preloader":function(){define(["dojo/_base/declare","dojo/Evented","dojo/on","dojo/_base/lang"],function(e,k,h,a){return e("mycore.common.Preloader",k,{list:null,_totalWeight:0,_currentWeight:0,constructor:function(a){this.list=[];e.safeMixin(this,a)},preload:function(){var d=this.list.length;
h.emit(this,"started",{size:d});for(var b=this._totalWeight=this._currentWeight=0;b<d;b++){var g=this.list[b];if(!g.getPreloadWeight){console.error("No preload weight defined for:");return}if(!g.preload){console.error("Object is not preloadable:");return}g.getPreloadName?this._totalWeight+=g.getPreloadWeight():console.warn("Warning: no preload name defined for:")}for(b=0;b<d;b++)g=this.list[b],h.emit(this,"preloadObject",{name:g.getPreloadName()}),g.preload(a.hitch({instance:this,object:g},this._onLoad))},
_onLoad:function(){if(!this.instance||!this.object)console.error("Invalid scope of _onLoad:");else{this.instance._currentWeight+=this.object.getPreloadWeight();var a=100*(this.instance._currentWeight/this.instance._totalWeight),b=this.instance.list.indexOf(this.object);this.instance.list.splice(b,1);h.emit(this.instance,"preloadObjectFinished",{name:this.object.getPreloadName(),progress:a});0==this.instance.list.length&&h.emit(this.instance,"finished")}}})})},"mycore/dijit/ExceptionDialog":function(){define("dojo/_base/declare mycore/dijit/SimpleDialog dojo/dom-class dojo/dom-construct dojo/dom-attr dojo/dom-style dojo/on dojo/_base/lang".split(" "),
function(e,k,h,a,d,b,g,f){return e("mycore.dijit.ExceptionDialog",k,{exception:null,exceptionI18nCache:{de:{"mycore.dijit.exceptionDialog.title":"Fehler","mycore.dijit.exceptionDialog.text":"Es ist ein Fehler aufgetreten, bitte kontaktieren Sie Ihren Administrator."},en:{"mycore.dijit.exceptionDialog.title":"Exception","mycore.dijit.exceptionDialog.text":"An error occur, please contact your administrator."}},constructor:function(c){null==c.exception?console.error("No exception given in args"):(this.i18nStore.mixin(this.exceptionI18nCache,
!1),this.i18nTitle="mycore.dijit.exceptionDialog.title",this.i18nText="mycore.dijit.exceptionDialog.text",e.safeMixin(this,c))},createContent:function(){this.inherited(arguments);var c=a.create("div",{style:"color: red",innerHTML:"\x3cp\x3e"+("["+this.exception.lineNumber+"] "+this.exception.fileName)+"\x3cbr /\x3e"+this.exception.message+"\x3c/p\x3e"});this.content.appendChild(c)}})})},"mycore/dijit/Preloader":function(){define("dojo/_base/declare dijit/_Widget dijit/_Templated dojo/text!./templates/Preloader.html dojo/_base/lang dojo/on dijit/ProgressBar".split(" "),
function(e,k,h,a,d,b){return e("mycore.dijit.Preloader",[k,h],{templateString:a,widgetsInTemplate:!0,baseClass:"mycorePreloader",preloader:null,showText:!1,text:"",constructor:function(b){b.preloader||console.error("No preloader defined. e.g. new mycore.dijit.Preloader({preloader: new mycore.common.Preloader()})");e.safeMixin(this,b)},create:function(){this.inherited(arguments);this.updateText();b(this.preloader,"preloadObjectFinished",d.hitch(this,function(b){this.progressBar.update({maximum:100,
progress:b.progress});this.updateText()}))},updateText:function(){if(this.showText){for(var b="",a=this.preloader.list,c=0;c<a.length;c++)b+=a[c].getPreloadName(),b+=c+1!=a.length?", ":"";this.text.innerHTML=0==b.length?"":"["+b+"]"}}})})},"mycore/common/I18nResolver":function(){define("dojo/_base/declare dojo/dom-attr dojo/dom-construct mycore/util/DOMUtil mycore/util/DOJOUtil dijit/Tooltip".split(" "),function(e,k,h,a,d){return e("mycore.common.I18nResolver",null,{store:null,constructor:function(b){e.safeMixin(this,
b)},resolve:function(b,g){a.isNode(g)?this.resolveNode(b,g):d.isWidget(g)?d.isWidgetClass(g,"dijit.form.Select")?this.resolveSelect(b,g):d.isWidgetClass(g,"dijit.form.ValidationTextBox")?this.resolveValidationTextBox(b,g):d.isWidgetClass(g,"dijit.form.CheckBox")?this.resolveCheckBox(b,g):this.resolveWidget(b,g):console.error("Cannot resolve object:")},resolveNode:function(b,a){this.store.getI18nText({language:b,label:k.get(a,"i18n"),load:function(b){a.innerHTML=b}})},resolveWidget:function(b,a){var f=
a.get("i18n");f&&this.store.getI18nText({language:b,label:f,load:function(c){a.set("label",c)}});if(a.getChildren)for(var f=a.getChildren(),c=0;c<f.length;c++)this.resolveWidget(b,f[c])},resolveValidationTextBox:function(b,a){a.i18nPromptMessage&&this.store.getI18nText({language:b,label:a.i18nPromptMessage,load:function(b){a.set("promptMessage",b)}});a.i18nInvalidMessage&&this.store.getI18nText({language:b,label:a.i18nInvalidMessage,load:function(b){a.set("invalidMessage",b)}});a.i18nMissingMessage&&
this.store.getI18nText({language:b,label:a.i18nMissingMessage,load:function(b){a.set("missingMessage",b)}})},resolveSelect:function(b,a){for(var f=a.getOptions(),c=0;c<f.length;c++){var d=f[c],p=d.i18n?d.i18n:null;null!=p&&this.store.getI18nText({language:b,label:p,callbackData:{select:a,option:d},load:function(c,a){a.option.label=c;a.select.updateOption(a.option)}})}},resolveCheckBox:function(a,d){this.store.getI18nText({language:a,label:d.get("i18n"),load:function(a){a=h.create("label",{"for":d.get("id"),
innerHTML:a});h.place(a,d.domNode,"after")}})},resolveTooltip:function(a,d){this.store.getI18nText({language:a,label:d.i18nTooltip,load:function(a){(new dijit.Tooltip({label:a})).addTarget(d.domNode)}})}})})},"dojo/NodeList-manipulate":function(){define(["./query","./_base/lang","./_base/array","./dom-construct","./NodeList-dom"],function(e,k,h,a){function d(c){var a="";c=c.childNodes;for(var b=0,f;f=c[b];b++)8!=f.nodeType&&(a=1==f.nodeType?a+d(f):a+f.nodeValue);return a}function b(c){for(;c.childNodes[0]&&
1==c.childNodes[0].nodeType;)c=c.childNodes[0];return c}function g(c,b){"string"==typeof c?(c=a.toDom(c,b&&b.ownerDocument),11==c.nodeType&&(c=c.childNodes[0])):1==c.nodeType&&c.parentNode&&(c=c.cloneNode(!1));return c}var f=e.NodeList;k.extend(f,{_placeMultiple:function(c,b){for(var f="string"==typeof c||c.nodeType?e(c):c,d=[],g=0;g<f.length;g++)for(var h=f[g],k=this.length,l=k-1,m;m=this[l];l--)0<g&&(m=this._cloneNode(m),d.unshift(m)),l==k-1?a.place(m,h,b):h.parentNode.insertBefore(m,h),h=m;d.length&&
(d.unshift(0),d.unshift(this.length-1),Array.prototype.splice.apply(this,d));return this},innerHTML:function(c){return arguments.length?this.addContent(c,"only"):this[0].innerHTML},text:function(c){if(arguments.length){for(var b=0,f;f=this[b];b++)1==f.nodeType&&(a.empty(f),f.appendChild(f.ownerDocument.createTextNode(c)));return this}for(var g="",b=0;f=this[b];b++)g+=d(f);return g},val:function(c){if(arguments.length){for(var a=k.isArray(c),b=0,f;f=this[b];b++){var d=f.nodeName.toUpperCase(),g=f.type,
e=a?c[b]:c;if("SELECT"==d){d=f.options;for(g=0;g<d.length;g++){var l=d[g];l.selected=f.multiple?-1!=h.indexOf(c,l.value):l.value==e}}else"checkbox"==g||"radio"==g?f.checked=f.value==e:f.value=e}return this}if((f=this[0])&&1==f.nodeType){c=f.value||"";if("SELECT"==f.nodeName.toUpperCase()&&f.multiple){c=[];d=f.options;for(g=0;g<d.length;g++)l=d[g],l.selected&&c.push(l.value);c.length||(c=null)}return c}},append:function(c){return this.addContent(c,"last")},appendTo:function(c){return this._placeMultiple(c,
"last")},prepend:function(c){return this.addContent(c,"first")},prependTo:function(c){return this._placeMultiple(c,"first")},after:function(c){return this.addContent(c,"after")},insertAfter:function(c){return this._placeMultiple(c,"after")},before:function(c){return this.addContent(c,"before")},insertBefore:function(c){return this._placeMultiple(c,"before")},remove:f.prototype.orphan,wrap:function(c){if(this[0]){c=g(c,this[0]);for(var a=0,f;f=this[a];a++){var d=this._cloneNode(c);f.parentNode&&f.parentNode.replaceChild(d,
f);b(d).appendChild(f)}}return this},wrapAll:function(c){if(this[0]){c=g(c,this[0]);this[0].parentNode.replaceChild(c,this[0]);c=b(c);for(var a=0,f;f=this[a];a++)c.appendChild(f)}return this},wrapInner:function(c){if(this[0]){c=g(c,this[0]);for(var a=0;a<this.length;a++){var b=this._cloneNode(c);this._wrap(k._toArray(this[a].childNodes),null,this._NodeListCtor).wrapAll(b)}}return this},replaceWith:function(c){c=this._normalize(c,this[0]);for(var a=0,b;b=this[a];a++)this._place(c,b,"before",0<a),b.parentNode.removeChild(b);
return this},replaceAll:function(c){c=e(c);for(var a=this._normalize(this,this[0]),b=0,f;f=c[b];b++)this._place(a,f,"before",0<b),f.parentNode.removeChild(f);return this},clone:function(){for(var c=[],a=0;a<this.length;a++)c.push(this._cloneNode(this[a]));return this._wrap(c,this,this._NodeListCtor)}});f.prototype.html||(f.prototype.html=f.prototype.innerHTML);return f})},"mycore/mycore-dojo-all":function(){define(["./common/common-all","./dijit/dijit-all","./util/util-all"],function(){console.warn("mycore-dojo-all may include much more code than your application actually requires. We strongly recommend that you investigate a custom build or the web build tool");
return{}})},"mycore/dijit/AbstractDialog":function(){define("dojo/_base/declare dijit/Dialog dojo/on dojo/_base/lang dojo/dom-class dojo/dom-construct dojo/dom-style dijit/form/Button mycore/common/I18nStore mycore/common/I18nResolver".split(" "),function(e,k,h,a,d,b,g){return e("mycore.dijit.AbstractDialog",k,{Type:{ok:"ok",cancel:"cancel",okCancel:"okCancel",yesNo:"yesNo",yesNoCancel:"yesNoCancel"},defaultI18nCache:{de:{"mycore.dijit.dialog.ok":"Ok","mycore.dijit.dialog.cancel":"Abbruch","mycore.dijit.dialog.yes":"Ja",
"mycore.dijit.dialog.no":"Nein"},en:{"mycore.dijit.dialog.ok":"Ok","mycore.dijit.dialog.cancel":"Cancel","mycore.dijit.dialog.yes":"Yes","mycore.dijit.dialog.no":"No"}},i18nStore:null,i18nTitle:null,type:null,internalDialog:null,content:null,language:null,okButton:null,cancelButton:null,yesButton:null,noButton:null,additionalData:null,created:!1,constructor:function(a){this.language="de";this.i18nTitle="undefined";this.type=this.Type.ok;a.i18nStore?a.i18nStore.mixin(this.defaultI18nCache):this.i18nStore=
new mycore.common.I18nStore({cache:this.defaultI18nCache});e.safeMixin(this,a)},setTitle:function(a){this.i18nTitle=a;this.updateTitle(this.language)},getTitle:function(){return this.internalDialog.get("title")},_create:function(){this.internalDialog=new dijit.Dialog;this.okButton=new dijit.form.Button({i18n:"mycore.dijit.dialog.ok"});this.cancelButton=new dijit.form.Button({i18n:"mycore.dijit.dialog.cancel"});this.yesButton=new dijit.form.Button({i18n:"mycore.dijit.dialog.yes"});this.noButton=new dijit.form.Button({i18n:"mycore.dijit.dialog.no"});
d.add(this.internalDialog.domNode,"mycoreDialog");this.content=b.create("div");d.add(this.content,"content");this.internalDialog.set("content",this.content);var f=b.create("div");d.add(f,"controls");b.place(f,this.internalDialog.domNode);this.type==this.Type.ok?f.appendChild(this.okButton.domNode):this.type==this.Type.cancel?f.appendChild(this.cancelButton.domNode):this.type==this.Type.okCancel?(f.appendChild(this.cancelButton.domNode),f.appendChild(this.okButton.domNode)):this.type==this.Type.yesNo?
(f.appendChild(this.noButton.domNode),f.appendChild(this.yesButton.domNode)):this.type==this.Type.yesNoCancel&&(f.appendChild(this.cancelButton.domNode),f.appendChild(this.noButton.domNode),f.appendChild(this.yesButton.domNode));h(this.okButton,"click",a.hitch(this,function(){this.onBeforeOk();this.internalDialog.hide();this.onOk()}));h(this.cancelButton,"click",a.hitch(this,function(){this.onBeforeCancel();this.internalDialog.hide();this.onCancel()}));h(this.yesButton,"click",a.hitch(this,function(){this.onBeforeYes();
this.internalDialog.hide();this.onYes()}));h(this.noButton,"click",a.hitch(this,function(){this.onBeforeNo();this.internalDialog.hide();this.onNo()}));this.created=!0},show:function(){this.created||(this._create(),this.createContent&&(this.createContent(),this.updateLang(this.language)));this.beforeShow&&this.beforeShow();this.internalDialog.show()},updateLang:function(a){this.language=a;if(this.created){var c=new mycore.common.I18nResolver({store:this.i18nStore});c.resolve(a,this.okButton);c.resolve(a,
this.cancelButton);c.resolve(a,this.yesButton);c.resolve(a,this.noButton);this.updateTitle(a)}},updateTitle:function(b){this.i18nTitle&&this.i18nStore.getI18nText({language:b,label:this.i18nTitle,load:a.hitch(this,function(a){this.internalDialog.set("title",a)})})},setWidth:function(a){g.set(this.content,"width",a+"px")},onBeforeOk:function(){},onBeforeCancel:function(){},onBeforeYes:function(){},onBeforeNo:function(){},onOk:function(){},onCancel:function(){},onYes:function(){},onNo:function(){}})})},
"mycore/common/UndoManager":function(){define(["dojo/_base/declare","dojo/on"],function(e,k){return e("mycore.common.UndoManager",null,{pointer:-1,limit:20,list:null,onExecute:!1,blockEvent:!1,_forceNoMergeSwitch:!1,constructor:function(h){this.list=[];e.safeMixin(this,h)},add:function(e){this.pointer<this.list.length-1&&(this.list=this.list.slice(0,this.pointer+1));this.list.length>=this.limit&&(this.list=this.list.slice(1),this.pointer--);var a=this.list[this.pointer];!this._forceNoMergeSwitch&&
null!=a&&e.merge&&a.merge&&a.isAssociated(e)?(a.merge(e),k.emit(this,"merged",{mergedEdit:e,undoableEdit:a})):(this.list.push(e),e.undoManager=this,this.pointer++,k.emit(this,"add",{undoableEdit:e}));this._forceNoMergeSwitch=!1},canUndo:function(){return 0<=this.pointer},canRedo:function(){return this.pointer<this.list.length-1},undo:function(){this.canUndo()&&(this.onExecute=!0,this.list[this.pointer].undo(),this.onExecute=!1,this.pointer--,this.forceNoMerge(),k.emit(this,"undo"))},redo:function(){this.canRedo()&&
(this.pointer++,this.onExecute=!0,this.list[this.pointer].redo(),this.onExecute=!1,this.forceNoMerge(),k.emit(this,"redo"))},forceNoMerge:function(){this._forceNoMergeSwitch=!0}})})},"mycore/util/DOMUtil":function(){define("exports dojo/_base/declare dojo/_base/lang dojo/Deferred dojo/dom-construct dojo/dom-attr dojo/query dojo/NodeList-manipulate".split(" "),function(e,k,h,a,d,b,g){e.isNode=function(a){return"object"===typeof Node?a instanceof Node:a&&"object"===typeof a&&"number"===typeof a.nodeType&&
"string"===typeof a.nodeName};e.isElement=function(a){return"object"===typeof HTMLElement?a instanceof HTMLElement:a&&"object"===typeof a&&1===a.nodeType&&"string"===typeof a.nodeName};e.loadCSS=function(b){var c=new a,e=d.create("link",{rel:"stylesheet",type:"text/css",href:b,onload:function(){c.resolve("success")},onerror:function(a){c.reject({error:a,href:b})}});g("head").append(e);return c.promise};e.updateBodyTheme=function(a){null==a&&(a="claro");g("body").forEach(function(a){b.set(a,"class",
"claro")})}})},"mycore/common/UndoableEdit":function(){define(["dojo/_base/declare"],function(e){return e("mycore.common.UndoableEdit",null,{undoManager:null,getLabel:function(){return"no label defined"},undo:function(){},redo:function(){}})})},"mycore/dijit/SimpleDialog":function(){define("dojo/_base/declare mycore/dijit/AbstractDialog dojo/dom-class dojo/dom-construct dojo/dom-attr dojo/dom-style dojo/on dojo/_base/lang".split(" "),function(e,k,h,a,d,b,g,f){return e("mycore.dijit.SimpleDialog",
k,{i18nText:"undefined",imageURL:null,textTd:null,imageElement:null,setText:function(a){this.i18nText=a;this.updateText(this.language)},setImage:function(a){this.image=a;this.updateImage()},createContent:function(){var c=a.create("table"),b=a.create("tr"),d=a.create("td");this.textTd=a.create("td");this.imageElement=a.create("img",{style:"padding-right: 10px;"});this.content.appendChild(c);c.appendChild(b);b.appendChild(d);b.appendChild(this.textTd);d.appendChild(this.imageElement);this.setImage(this.imageURL);
this.setText(this.i18nText)},updateText:function(a){this.i18nText&&this.i18nStore.getI18nText({language:a,label:this.i18nText,load:f.hitch(this,function(a){d.set(this.textTd,{innerHTML:a})})})},updateImage:function(){null==this.imageURL?b.set(this.imageElement,"display","none"):b.set(this.imageElement,"display","block");d.set(this.imageElement,{src:this.imageURL})},updateLang:function(a){this.inherited(arguments);this.created&&this.updateText(a)}})})},"mycore/common/I18nStore":function(){define(["dojo/_base/declare",
"dojo/_base/lang","dojo/_base/xhr"],function(e,k,h){return e("mycore.common.I18nStore",null,{cache:null,url:"",constructor:function(a){this.cache=[];e.safeMixin(this,a)},fetch:function(a,d){void 0==this.cache[a]&&(this.cache[a]={});var b={url:this.url+a+"/"+d+"*",sync:!0,handleAs:"json",load:k.hitch(this,function(b){for(var d in b)this.cache[a][d]=b[d]}),error:function(a){}};h.get(b)},mixin:function(a,d){d=void 0===d?!0:d;for(var b in a)if(this.cache[b])for(var g in a[b]){if(d||!this.cache[b][g])this.cache[b][g]=
a[b][g]}else this.cache[b]=a[b]},_getI18nTextFromCache:function(a){if(!a.language)throw console.error("Undefined language"),"Undefined language";if(!a.label)throw console.error("Undefined label"),"Undefined label";if(void 0==this.cache[a.language])throw a="There are no i18n texts for language '"+a.language+"' defined!",console.error(a),a;return this.cache[a.language][a.label]},getI18nTextFromCache:function(a){var d=this._getI18nTextFromCache(a);return void 0==d?"undefined ('"+a.label+"')":d},getI18nText:function(a){if(!a.load)throw console.error("Undefined load method"),
"Undefined load method";var d=this._getI18nTextFromCache(a);void 0!=d?a.load(d,a.callbackData):this.get18nTextFromServer(a)},get18nTextFromServer:function(a){var d={url:this.url+a.language+"/"+a.label,load:k.hitch(this,function(b){this.cache[language][label]=b;a.load&&a.load(b,a.callbackData)}),error:function(b){a.error?a.error(b,a.callbackData):console.error("error while retrieving i18n text:")}};h.get(d)}})})},"mycore/dijit/I18nRow":function(){define("dojo/_base/declare mycore/dijit/RepeaterRow dijit/_Templated dojo/text!./templates/I18nRow.html dojo/on dojo/_base/lang dojo/dom-construct mycore/util/DOJOUtil dijit/form/TextBox dijit/form/Select dijit/form/Textarea mycore/common/EventDelegator".split(" "),
function(e,k,h,a,d,b,g,f){return e("mycore.dijit.I18nRow",[k,h],{templateString:a,widgetsInTemplate:!0,eventDelegator:null,baseClass:"i18nRow",constructor:function(a){e.safeMixin(this,a)},create:function(a){this.inherited(arguments);this._setLanguages(a.languages?a.languages:["de"]);this.set("value",a.initialValue);this.eventDelegator=new mycore.common.EventDelegator({source:this,delegate:!1,getEventObject:b.hitch(this,function(a){return{row:this,value:this.get("value")}})});this.eventDelegator.register("lang",
this.lang);this.eventDelegator.register("text",this.text);this.eventDelegator.register("description",this.description);setTimeout(b.hitch(this,function(){this.eventDelegator.startDelegation()}),1)},_setValueAttr:function(a,b){null!=a&&(a=this._normalize(a),this.equals(a)||(null!=this.eventDelegator&&(this.lang.get("value")!=a.lang&&this.eventDelegator.block("lang"),this.text.get("value")!=a.text&&this.eventDelegator.block("text"),this.description.get("value")!=a.description&&this.eventDelegator.block("description"),
(b||void 0===b)&&this.eventDelegator.fireAfterLastBlock()),this.containsLanguage(a.lang)||this.lang.addOption({value:a.lang,label:a.lang}),this.lang.set("value",a.lang),this.text.set("value",a.text),this.description.set("value",a.description)))},_getValueAttr:function(){return{lang:this.lang.get("value"),text:this.text.get("value"),description:this.description.get("value")}},_setDisabledAttr:function(a){this.lang.set("disabled",a);this.text.set("disabled",a);this.description.set("disabled",a);this.inherited(arguments)},
_setLanguages:function(a){for(var b=this.lang.get("value"),d=this.lang.getOptions(),f=[],g=0;g<a.length;g++)f.push({value:a[g],label:a[g]});null!=this.eventDelegator&&this.eventDelegator.block("lang");this.lang.removeOption(d);this.lang.addOption(f);""!=b&&!this.containsLanguage(b)&&this.lang.addOption({value:b,label:b});this.lang.set("value",b)},receive:function(a){a.id&&"resetLang"==a.id&&a.languages&&this._setLanguages(a.languages)},_normalize:function(a){return{lang:a.lang?a.lang:null,text:a.text?
a.text:"",description:a.description?a.description:""}},equals:function(a){a=this._normalize(a);return this.lang.get("value")==a.lang&&this.text.get("value")==a.text&&this.description.get("value")==a.description},containsLanguage:function(a){for(var b=this.lang.getOptions(),d=0;d<b.length;d++)if(b[d].value==a)return!0;return!1}})})},"mycore/util/DOJOUtil":function(){define(["exports","dojo/_base/declare","dojo/_base/array"],function(e,k,h){e.isWidget=function(a){return"object"===typeof a&&void 0!=
a.baseClass&&void 0!=a.declaredClass};e.isWidgetClass=function(a,d){return this.isWidget(a)&&a.declaredClass==d};e.instantiate=function(a,d){var b,g=window;b=a.split(".");for(var f=0;f<b.length;f++)if(g=g[b[f]],void 0==g)throw Error("Undefined class "+a);b=function(){};b.prototype=g.prototype;b=new b;g.apply(b,d);b.constructor=g;return b};e.arrayUnique=function(a,d){if(null==a||null==d)return null==a&&null==d?null:null==a?d:a;var b=[],g=a.concat(d);h.forEach(g,function(a){null==a||-1<h.indexOf(b,
a)||b.push(a)});return b};e.arrayEqual=function(a,d){return!!a&&!!d&&!(a<d||d<a)};e.deepEqual=function(a,d){function b(a,c){if(typeof a!=typeof c)return!1;if("function"==typeof a||"object"==typeof a){var d=0,e;for(e in a)d++;for(e in c)d--;if(0!=d)return!1;for(var h in a)if(g=b(a[h],c[h]),!g)return!1;return g}return a==c}var g=!0;return b(a,d)}})},"mycore/common/UndoableMergeEdit":function(){define(["dojo/_base/declare","dojo/_base/lang","mycore/common/UndoableEdit"],function(e,k,h){return e("mycore.common.UndoableMergeEdit",
h,{timeout:1E3,_timeoutMilli:null,constructor:function(a){this._timeoutMilli=(new Date).getTime();e.safeMixin(this,a)},merge:function(a){},isAssociated:function(a){var d=!(this._timeoutMilli+this.timeout<a._timeoutMilli);this._timeoutMilli=a._timeoutMilli;return d}})})},"mycore/common/EventDelegator":function(){define(["dojo/_base/declare","dojo/Evented","dojo/on","dojo/_base/lang","mycore/util/DOJOUtil"],function(e,k,h,a,d){return e("mycore.common.EventDelegator",k,{delegate:!0,event:"change",source:null,
objects:null,_signals:null,_objectsToBlock:null,_fireAfterLastBlock:!1,constructor:function(a){a.source&&(this.objects={},this._signals={},this._objectsToBlock=[],e.safeMixin(this,a))},startDelegation:function(){this.delegate=!0},stopDelegation:function(){this.delegate=!1},register:function(b,d){this.objects[b]=d;this._signals[b]=h(d,this.event,a.hitch({instance:this,id:b},this._handleEvent))},unregister:function(a){delete this.objects[a];this._signals[a].remove();delete this._signals[a];delete this._objectsToBlock[a]},
block:function(a){"string"===typeof a?this._objectsToBlock.push(a):"[object Array]"===Object.prototype.toString.call(a)?this._objectsToBlock=this._objectsToBlock.concat(a):console.error("Invalid argument: call block() with json or string "+a);this._objectsToBlock=d.arrayUnique(this._objectsToBlock,[])},getEventObject:function(){return{}},_handleEvent:function(b){var d=this.instance._objectsToBlock.indexOf(this.id);-1!=d?(this.instance._objectsToBlock.splice(d,1),this.instance._fireAfterLastBlock&&
0==this.instance._objectsToBlock.length&&(this.instance._fireAfterLastBlock=!1,a.hitch(this.instance,this.instance.fire)(b))):this.instance.delegate&&a.hitch(this.instance,this.instance.fire)(b)},fire:function(a){h.emit(this.source,this.event,this.getEventObject(a))},fireAfterLastBlock:function(){this._fireAfterLastBlock=!0}})})},"dijit/_Templated":function(){define("./_WidgetBase ./_TemplatedMixin ./_WidgetsInTemplateMixin dojo/_base/array dojo/_base/declare dojo/_base/lang dojo/_base/kernel".split(" "),
function(e,k,h,a,d,b,g){b.extend(e,{waiRole:"",waiState:""});return d("dijit._Templated",[k,h],{widgetsInTemplate:!1,constructor:function(){g.deprecated(this.declaredClass+": dijit._Templated deprecated, use dijit._TemplatedMixin and if necessary dijit._WidgetsInTemplateMixin","","2.0")},_processNode:function(b,d){var g=this.inherited(arguments),e=d(b,"waiRole");e&&b.setAttribute("role",e);(e=d(b,"waiState"))&&a.forEach(e.split(/\s*,\s*/),function(a){-1!=a.indexOf("-")&&(a=a.split("-"),b.setAttribute("aria-"+
a[0],a[1]))});return g}})})},"mycore/util/util-all":function(){define(["./DOJOUtil","./DOMUtil"],function(){console.warn("util-all may include much more code than your application actually requires. We strongly recommend that you investigate a custom build or the web build tool");return{}})},"mycore/common/CompoundEdit":function(){define(["dojo/_base/declare","mycore/common/UndoableEdit"],function(e,k){return e("mycore.common.CompoundEdit",k,{edits:null,constructor:function(h){this.edits=[];e.safeMixin(this,
h)},addEdit:function(e){this.edits.push(e)},undo:function(){for(var e=this.edits.length-1;0<=e;e--)this.edits[e].undo()},redo:function(){for(var e=0;e<this.edits.length;e++)this.edits[e].redo()}})})},"mycore/common/I18nManager":function(){define(["dojo/_base/declare","dojo/cookie","dojo/_base/json","mycore/common/I18nStore","mycore/common/I18nResolver"],function(e,k,h){e=e("mycore.common.I18nManager",[],{store:null,resolver:null,language:null,languages:null,constructor:function(a){this.language=k("i18n.language")?
k("i18n.language"):"de";this.languages=k("i18n.languages")?h.fromJson(k("i18n.languages")):["de","en"]},init:function(a){this.store=a;this.resolver=new mycore.common.I18nResolver({store:this.store})},setLanguage:function(a){this.language=a;k("i18n.language",this.language,{expires:365})},setLanguages:function(a){this.languages=a;k("i18n.languages",h.toJson(this.languages),{expires:365})},getLanguage:function(){return this.language},getLanguages:function(){return this.languages},fetch:function(a){this.store.fetch(this.language,
a)},get:function(a){a.language||(a.language=this.language);this.store.getI18nText(a)},getFromCache:function(a){return this.store.getI18nTextFromCache({language:this.language,label:a})},resolve:function(a){this.resolver.resolve(this.language,a)},resolveTooltip:function(a){this.resolver.resolveTooltip(this.language,a)}});if(!a)var a=new e;return a})},"mycore/dijit/TextRow":function(){define("dojo/_base/declare mycore/dijit/RepeaterRow dojo/on dojo/_base/lang dojo/dom-construct dijit/form/TextBox".split(" "),
function(e,k,h,a,d){return e("mycore.dijit.TextRow",[k],{baseClass:"textRow",textBox:null,eventDelegator:null,constructor:function(b){this.inherited(arguments);this.textBox=new dijit.form.TextBox({value:this.initialValue,style:"width: 100%",intermediateChanges:!0});this.eventDelegator=new mycore.common.EventDelegator({source:this,delegate:!1,getEventObject:a.hitch(this,function(a){return{row:this,value:this.get("value")}})});this.eventDelegator.register("text",this.textBox);setTimeout(a.hitch(this,
function(){this.eventDelegator.startDelegation()}),1)},create:function(){this.inherited(arguments);this.addColumn(this.textBox.domNode)},_setValueAttr:function(a,d){d||void 0===d||this.eventDelegator.block("text");this.textBox.set("value",a)},_getValueAttr:function(){return this.textBox.get("value")},_setDisabledAttr:function(a){this.textBox.set("disabled",a);this.inherited(arguments)},equals:function(a){return this.textBox.get("value")==a}})})},"mycore/dijit/RepeaterRow":function(){define("dojo/_base/declare dijit/_Widget dijit/_Templated dojo/Evented dojo/text!./templates/RepeaterRow.html dojo/on dojo/_base/lang dojo/dom-construct dojo/dom-class dojo/dom-style dojo/json dijit/form/Button".split(" "),
function(e,k,h,a,d,b,g,f,c,n,p){return e("mycore.dijit.RepeaterRow",[k,h,a],{templateString:d,widgetsInTemplate:!0,disabled:!1,baseClass:"mycoreRepeaterRow",_repeater:null,initialValue:null,removeable:!0,constructor:function(a){null==a._repeater?console.error("No repeater set for this row. You should call addRow() in your repeater to create a row."):e.safeMixin(this,a)},create:function(){this.inherited(arguments);b(this.removeRow,"click",g.hitch(this,this._onRemove))},addColumn:function(a){var b=
f.create("td");f.place(a,b);f.place(b,this.control,"before");return b},getRepeater:function(){return this._repeater},_onRemove:function(){b.emit(this,"remove",{row:this})},_setDisabledAttr:function(a){this.disabled=a;this.removeRow.set("disabled",a);b.emit(this,"disable",{row:this,disabled:a})},_setRemovableAttr:function(a){this.removable=a;n.set(this.removeRow.domNode,"display",a?"block":"none")},_setValueAttr:function(a,b){},_getValueAttr:function(){},equals:function(a){return!1},receive:function(a){}})})},
"mycore/dijit/Repeater":function(){define("dojo/_base/declare dijit/_Widget dijit/_Templated dojo/Evented dojo/text!./templates/Repeater.html dojo/on dojo/_base/lang dojo/dom-construct dojo/dom-class mycore/util/DOJOUtil mycore/dijit/RepeaterRow mycore/dijit/PlainButton".split(" "),function(e,k,h,a,d,b,g,f,c,n){return e("mycore.dijit.Repeater",[k,h,a],{templateString:d,widgetsInTemplate:!0,baseClass:"mycoreRepeater",row:null,disabled:!1,_rows:null,minOccurs:0,head:null,constructor:function(a){!a.row||
!a.row.className?console.error("No row class is given. Create e.g. with {row: {class: 'my.sample.className'}}"):(this._rows=[],e.safeMixin(this,a))},create:function(){this.inherited(arguments);for(var a=0;a<this.minOccurs;a++)this._addRow({disabled:this.disabled});b(this.addRowButton,"click",g.hitch(this,this._onAdd))},_setValueAttr:function(a,b){for(var c=!1,d=0,e=0;e<a.length;e++){if(d<this._rows.length){var f=this._rows[d];f.equals(a[e])||(f.set("value",a[e],!1),c=!0)}else c=!0,this._addRow({initialValue:a[e],
disabled:this.disabled});d++}for(;this._rows.length>d;)c=!0,this._removeRow(this._rows[this._rows.length-1]);c&&(b||void 0===b)&&setTimeout(g.hitch(this,function(){this._onChange()}),1)},_getValueAttr:function(){for(var a=[],b=0;b<this._rows.length;b++)a.push(this._rows[b].get("value"));return a},addRow:function(a){a=this._addRow(a);this._onChange();return a},_addRow:function(a){var c=this.row.args?g.clone(this.row.args):{};g.mixin(c,a);null==c.removable&&(c.removable=this._rows.length>=this.minOccurs);
c._repeater=this;a=n.instantiate(this.row.className,[c]);b(a,"remove",g.hitch(this,this._onRemove));b(a,"change",g.hitch(this,this._onChange));f.place(a.domNode,this.addNode,"before");this._rows.push(a);return a},removeRow:function(a){this._removeRow(a);this._onChange()},_removeRow:function(a){var b=this.indexOf(a);this._rows.splice(b,1);a.destroy();return b},_setHeadAttr:function(a){null!=this.head&&f.destroy(this.head);this.head=a;f.place(this.head,this.tableBody,"first")},_onAdd:function(a){this.addRow()},
_onRemove:function(a){this.removeRow(a.row)},_onChange:function(){b.emit(this,"change",{source:this})},_setDisabledAttr:function(a){this.disabled=a;for(var c=0;c<this._rows.length;c++)this._rows[c].set("disabled",a);this.addRowButton.set("disabled",a);b.emit(this,"disable",{disabled:a})},indexOf:function(a){return this._rows.indexOf(a)},broadcast:function(a){for(var b=0;b<this._rows.length;b++)this._rows[b].receive(a)}})})},"url:mycore/dijit/templates/RepeaterRow.html":'\x3ctr class\x3d"${baseClass}"\x3e\n\t\x3c!-- add other table columns here --\x3e\n\t\x3ctd class\x3d"control remove" data-dojo-attach-point\x3d"control"\x3e\n\t\t\x3cbutton data-dojo-attach-point\x3d"removeRow" data-dojo-type\x3d"mycore.dijit.PlainButton"\n\t\t\t\tdata-dojo-props\x3d"showLabel: false, iconClass:\'icon-cancel\'"\x3e\x3c/button\x3e\n\t\x3c/td\x3e\n\x3c/tr\x3e\n',
"url:mycore/dijit/templates/Preloader.html":'\x3cdiv class\x3d"${baseClass}" role\x3d"progressbar" tabindex\x3d"-1"\x3e\n\t\x3cdiv data-dojo-attach-point\x3d"progressBar" data-dojo-type\x3d"dijit.ProgressBar"\x3e\x3c/div\x3e\n\t\x3cdiv data-dojo-attach-point\x3d"text" class\x3d"text"\x3e${text}\x3c/div\x3e\n    \x3cdiv data-dojo-attach-point\x3d"containerNode"\x3e\x3c/div\x3e\n\x3c/div\x3e',"url:mycore/dijit/templates/Repeater.html":'\x3cdiv class\x3d"${baseClass}" role\x3d"" tabindex\x3d"-1"\x3e\n\t\x3ctable class\x3d"contentNode"\x3e\n\t\t\x3ctbody data-dojo-attach-point\x3d"tableBody"\x3e\n\t\t\t\x3ctr data-dojo-attach-point\x3d"addNode"\x3e\n\t\t\t\t\x3ctd class\x3d"control add"\x3e\n\t\t\t\t\t\x3cbutton\tdata-dojo-attach-point\x3d"addRowButton" data-dojo-type\x3d"mycore.dijit.PlainButton"\n\t\t\t\t\t\t\tdata-dojo-props\x3d"showLabel: false, iconClass:\'icon-plus\'"\x3e\x3c/button\x3e\n\t\t\t\t\x3c/td\x3e\n\t\t\t\x3c/tr\x3e\n\t\t\x3c/tbody\x3e\n\t\x3c/table\x3e\n    \x3cdiv data-dojo-attach-point\x3d"containerNode"\x3e\x3c/div\x3e\n\x3c/div\x3e\n',
"url:mycore/dijit/templates/PlainButton.html":'\x3ca class\x3d"plain-button" role\x3d"presentation"\x3e\n\t\x3cspan data-dojo-attach-point\x3d"titleNode,focusNode" role\x3d"button" aria-labelledby\x3d"${id}_label"\n\t\tdata-dojo-attach-event\x3d"ondijitclick:_onClick"\x3e\n\t\t\x3cspan data-dojo-attach-point\x3d"iconNode" class\x3d"plain-icon"\x3e\x3c/span\x3e\n\t\t\x3cspan data-dojo-attach-point\x3d"containerNode" class\x3d"plain-label"\x3e${label}\x3c/span\x3e\n\t\x3c/span\x3e\n\t\x3cinput ${!nameAttrSetting} type\x3d"${type}" value\x3d"${value}" class\x3d"dijitOffScreen"\n\t\t\ttabIndex\x3d"-1" role\x3d"presentation" data-dojo-attach-point\x3d"valueNode"/\x3e\n\x3c/a\x3e\n',
"url:mycore/dijit/templates/I18nRow.html":'\x3ctr class\x3d"${baseClass}"\x3e\n\t\x3ctd class\x3d"content lang"\x3e\n\t\t\x3cselect data-dojo-attach-point\x3d"lang" data-dojo-type\x3d"dijit.form.Select"\x3e\x3c/select\x3e\n\t\x3c/td\x3e\n\t\x3ctd class\x3d"content text"\x3e\n\t\t\x3cinput data-dojo-attach-point\x3d"text" data-dojo-type\x3d"dijit.form.TextBox" data-dojo-props\x3d"intermediateChanges: true"/\x3e\n\t\x3c/td\x3e\n\t\x3ctd class\x3d"content description"\x3e\n\t\t\x3ctextarea data-dojo-attach-point\x3d"description" data-dojo-type\x3d"dijit.form.Textarea" data-dojo-props\x3d"intermediateChanges: true"\x3e\x3c/textarea\x3e\n\t\x3c/td\x3e\n\t\x3c!-- add other table columns here --\x3e\n\t\x3ctd class\x3d"control remove" data-dojo-attach-point\x3d"control"\x3e\n\t\t\x3cbutton data-dojo-attach-point\x3d"removeRow" data-dojo-type\x3d"mycore.dijit.PlainButton"\n\t\t\t\tdata-dojo-props\x3d"showLabel: false, iconClass:\'icon-cancel\'"\x3e\x3c/button\x3e\n\t\x3c/td\x3e\n\x3c/tr\x3e\n'}});
define("mycore/mycore-dojo",[],1);
//@ sourceMappingURL=mycore-dojo.js.map