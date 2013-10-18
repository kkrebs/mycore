var AccessTable = function(){
	var i18nKeys =[];
	var ruleSelectorInstance;
	var aclEditorInstance;
	
	return {
		init: 	function(accessAndRules, i18n, ruleSelector, aclEditor){
			i18nKeys = i18n;
			ruleSelectorInstance = ruleSelector;
			aclEditorInstance = aclEditor;
			
			var cla = this;
			$.each(accessAndRules.access, function(i, l) {
				cla.add(l.accessID, l.accessPool, l.rule, false);
			});
			ruleSelectorInstance.append("", $(".acle2-new-access-rule"));
			this.zebra();
			$("#acle2-access-table").stupidtable();
			$("#acle2-access-table").bind('aftertablesort', function () {
				cla.zebra();
			});
			$("#acle2-loading").hide();
			$("#acle2-ruleAllocation").show();
			$("a.tab").attr("data-toggle", "tab");
		},
		add: 	function(accessID, accessPool, rule, prepend){
			var tr = $("<tr class='acle2-table-access-entry'></tr>");
			tr.append("<td><input type='checkbox' class='icon-check-empty icon-large acle2-icon acle2-access-select'/></td>");
			tr.append("<td class='acle2-access-id acle2-table-access-entry-td' title='" +  accessID + "'>" + accessID + "</td>");
			tr.append("<td class='acle2-access-pool acle2-table-access-entry-td' title='" +  accessPool + "'>" + accessPool + "</td>");
			var td = $("<td class='acle2-access-rule-parent'></td>");
			ruleSelectorInstance.append(rule, td);
			tr.append(td);
			tr.append("<td></td>");
			if(prepend == true){
				tr.prependTo("#acle2-access-table > tbody");
				$("#acle2-access-table").find("th").data("sort-dir", null);
				$(".icon-caret-up").removeClass("icon-caret-up");
				$(".icon-caret-down").removeClass("icon-caret-down");
				this.zebra();
			}
			else{
				tr.appendTo("#acle2-access-table > tbody");
			}
		},
		remove:	function(data) {
			$(".acle2-delete").each(function() {
				var id = $(this).find(".acle2-access-id").text();
				var pool = $(this).find(".acle2-access-pool").text();
				var parent = $(this);
				$.each(data.access, function(i, l) {
					if(id == l.accessID && pool == l.accessPool){
						if(l.success == 1){
							parent.remove();
							return;
						}
						else{
							showAlert(i18nKeys["ACLE.alert.access.remove.error.1"] + id + " : " + pool + i18nKeys["ACLE.alert.access.remove.error.2"]);
							parent.removeClass("acle2-delete");
							return;
						}
					}
				});
				parent.removeClass("acle2-delete");
			});
			this.zebra();
		},
		edit: function(entry, accessID, accessPool, rule) {
			entry.find(".acle2-access-id").html(accessID);
			entry.find(".acle2-access-id").attr("title",accessID);
			entry.find(".acle2-access-id").removeClass("acle2-show-input");
			entry.find(".acle2-access-pool").html(accessPool);
			entry.find(".acle2-access-pool").attr("title",accessPool);
			entry.find(".acle2-access-pool").removeClass("acle2-show-input");
			entry.find(".acle2-access-rule").select2("val", rule);
		},
		editMulti: function(data) {
			$(".acle2-multi-edit").each(function() {
				var id = $(this).find(".acle2-access-id").text();
				var pool = $(this).find(".acle2-access-pool").text();
				var parent = $(this);
				$.each(data.access, function(i, l) {
					if(id == l.accessID && pool == l.accessPool){
						if(l.success == 1){
							parent.find(".acle2-access-rule").select2("val", l.accessRule);
							return;
						}
						else{
							showAlert(i18nKeys["ACLE.alert.access.edit.multi.error.1"] + id + " : " + pool + i18nKeys["ACLE.alert.access.edit.multi.error.2"]);
							parent.removeClass("acle2-multi-edit");
							return;
						}
					}
				});
				parent.removeClass("acle2-multi-edit");
			});
		},
		zebra:	function() {
			$("#acle2-access-table tr:not(.acle2-filter-hide)").each(function(i, row){
				$(row).removeClass("odd");
				if (i % 2 == 0){
					$(row).addClass("odd");
				}
			});
		}
	};
}