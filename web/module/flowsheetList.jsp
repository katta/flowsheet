<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View Encounters" otherwise="/login.htm" redirect="/module/flowsheet/flowsheet.list" />

<%@ include file="/WEB-INF/template/headerMinimal.jsp" %>

<script type="text/javascript">

	function mouseover(row, isDescription) {
		if (row.className.indexOf("searchHighlight") == -1) {
			row.className = "searchHighlight " + row.className;
			var other = getOtherRow(row, isDescription);
			other.className = "searchHighlight " + other.className;
		}
	}
	function mouseout(row, isDescription) {
		var c = row.className;
		row.className = c.substring(c.indexOf(" ") + 1, c.length);
		var other = getOtherRow(row, isDescription);
		c = other.className;
		other.className = c.substring(c.indexOf(" ") + 1, c.length);
	}
	function getOtherRow(row, isDescription) {
		if (isDescription == null) {
			var other = row.nextSibling;
			if (other.tagName == null)
				other = other.nextSibling;
		}
		else {
			var other = row.previousSibling;
			if (other.tagName == null)
				other = other.previousSibling;
		}
		return other;
	}
	function click(obsId) {
		document.location = "${pageContext.request.contextPath}/admin/observations/obs.form?obsId=" + obsId;
		return false;
	}
	
	function voidedClicked(input) {
		var reason = document.getElementById("voidReason");
		var voidedBy = document.getElementById("voidedBy");
		if (input.checked) {
			reason.style.display = "";
			if (voidedBy)
				voidedBy.style.display = "";
		}
		else {
			reason.style.display = "none";
			if (voidedBy)
				voidedBy.style.display = "none";
		}
	}

</script>

<style>
	#table th { text-align: left; }
	td.fieldNumber { 
		width: 5px;
		white-space: nowrap;
	}
	td.obsGroupMember {
		padding-left: 5px;
	}
	tr.voided {
		display: none;
	}
</style>

<c:forEach var="encounter" items="${encounters}" varStatus="encStatus">
	
	<c:set var="observations" value="${observationsMap[encounter.encounterId]}" />
	<c:set var="obsMap" value="${obsMapMap[encounter.encounterId]}" />
	<c:set var="editedObs" value="${editedObsMap[encounter.encounterId]}" />
	<c:set var="obsGroups" value="${obsGroupsMap[encounter.encounterId]}" />
	
	<c:if test="${encStatus.first}">
		<h2>${encounter.patient.patientName} - ${encounter.patient.patientIdentifier}</h2> <br/>
	
		<div class="boxHeader">
			<span style="float: right">
				<a href="#" id="showDescription" onClick="return toggleVisibility(document, 'div', 'description')"><spring:message code="general.toggle.description"/></a> |
				<a href="#" id="showVoided" onClick="return toggleRowVisibilityForClass('obs', 'voided', true);"><spring:message code="general.toggle.voided"/></a>
			</span>
			<b><spring:message code="flowsheet.titleBox"/></b>
		</div>
		
		<div class="box">
		<table cellspacing="0" cellpadding="2" width="98%" id="obs">
			<tr>
				<th class="fieldNumber"></th>
				<th><spring:message code="Obs.concept"/></th>
				<th><spring:message code="Obs.value"/></th>
				<th></th>
				<th><spring:message code="Obs.creator.or.changedBy"/></th>
			</tr>
	</c:if>
	
	<c:forEach items="${observations}" var="obs" varStatus="status">
		<c:if test="${status.first}">
			<tr>
				<td colspan="5" class="box"">
					${encounter.encounterType}
					(<a href="${pageContext.request.contextPath}/admin/forms/formEdit.form?formId=${encounter.form.formId}">#${encounter.form.formId}</a>) |
					<openmrs:formatDate date="${encounter.encounterDatetime}" type="medium" /> |
					${encounter.location} |
					<a href="${pageContext.request.contextPath}/admin/users/user.form?userId=${encounter.provider.userId}">${encounter.provider.firstName} ${encounter.provider.middleName} ${encounter.provider.lastName}</a>
				</td>
			</tr>
		</c:if>

		<c:set var="field" value="${obsMap[obs.obsId]}"/>
		<c:choose>
			<c:when test="${obs.obsGroupId != null}">
				<tr class="obsGroupHeader">
					<td>${field.fieldNumber}<c:if test="${field.fieldPart != null && field.fieldPart != ''}">.${field.fieldPart}</c:if></td>
					<td colspan="4">${field.field.concept.name.name}</td>
				</tr>
				<tr>
					<td colspan="5"></td>
				</tr>
				<c:forEach items="${obsGroups[obs.obsGroupId]}" var="groupObs" varStatus="groupStatus">
					<tr class="<c:if test="${groupObs.voided}">voided</c:if>" onmouseover="mouseover(this)" onmouseout="mouseout(this)" onclick="click('${groupObs.obsId}')">
						<td class="fieldNumber"></td>
						<td class="obsGroupMember"><a href="${pageContext.request.contextPath}/admin/observations/obs.form?obsId=${groupObs.obsId}" onclick="return click('${groupObs.obsId}')">${groupObs.concept.name.name}</a></td>
						<td>${groupObs.valueAsString[locale]}</td>
						<td valign="middle" align="right">
							<c:if test="${fn:contains(editedObs, groupObs.obsId)}"><img src="${pageContext.request.contextPath}/images/alert.gif" title='<spring:message code="Obs.edited"/>' /></c:if>
							<c:if test="${groupObs.comment != null && groupObs.comment != ''}"><img src="${pageContext.request.contextPath}/images/note.gif" title="${groupObs.comment}" /></c:if>
						</td>
						<td style="white-space: nowrap;">
							${groupObs.creator.firstName} ${groupObs.creator.lastName} -
							<openmrs:formatDate date="${groupObs.dateCreated}" type="medium" />
						</td>
					</tr>
					<tr class="<c:if test="${groupObs.voided}">voided </c:if><c:choose><c:when test="${status.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>" onmouseover="mouseover(this, true)" onmouseout="mouseout(this, true)" onclick="click('${groupObs.obsId}')">
						<td></td><td colspan="4" class="obsGroupMember"><div class="description">${groupObs.concept.name.description}</div></td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr class="<c:if test="${obs.voided}">voided </c:if><c:choose><c:when test="${count % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>" onmouseover="mouseover(this)" onmouseout="mouseout(this)" onclick="click('${obs.obsId}')">
					<td class="fieldNumber">${field.fieldNumber}<c:if test="${field.fieldPart != null && field.fieldPart != ''}">.${field.fieldPart}</c:if></td>
					<td><a href="${pageContext.request.contextPath}/admin/observations/obs.form?obsId=${obs.obsId}" onclick="return click('${obs.obsId}')">${obs.concept.name.name}</a></td>
					<td>${obs.valueAsString[locale]}</td>
					<td valign="middle" align="right">
						<c:if test="${fn:contains(editedObs, obs.obsId)}"><img src="${pageContext.request.contextPath}/images/alert.gif" title='<spring:message code="Obs.edited"/>' /></c:if>
						<c:if test="${obs.comment != null && obs.comment != ''}"><img src="${pageContext.request.contextPath}/images/note.gif" title="${obs.comment}" /></c:if>
					</td>
					<td style="white-space: nowrap;">
						${obs.creator.firstName} ${obs.creator.lastName} -
						<openmrs:formatDate date="${obs.dateCreated}" type="medium" />
					</td>
				</tr>
				<tr class="<c:if test="${obs.voided}">voided </c:if><c:choose><c:when test="${status.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>" onmouseover="mouseover(this, true)" onmouseout="mouseout(this, true)" onclick="click('${obs.obsId}')">
					<td colspan="5"><div class="description">${obs.concept.name.description}</div></td>
				</tr>
			</c:otherwise>
		</c:choose>
		
	</c:forEach>
	
	<c:if test="${encStatus.last}">
		</table>
		</div>
		
		<br/>
		<br/>
	</c:if>
	
</c:forEach>

<script type="text/javascript">
	toggleVisibility(document, "div", "description");
	toggleRowVisibilityForClass("obs", "voided", true);
</script>

<script type="text/javascript">

<%@ include file="/WEB-INF/template/footerMinimal.jsp" %>