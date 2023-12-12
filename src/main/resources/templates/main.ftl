<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <div>
        <@l.logout />
        <span><a href="/user">User list</a></span>
    </div>
    <div>
        <form method="post" action="/add">
            <input type="text" name="messageText" placeholder="Введите сообщение"/>
            <input type="text" name="tag" placeholder="Введите тег"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Добавить</button>
        </form>
    </div>

    <div>Список сообщений</div>
    <form method="get" action="/main">
        <input type="text" name="filter" value="${filter}">
        <button type="submit">Найти</button>
    </form>
    <#list messages as message>
        <div>
            <span>${message.text}</span>
            <i>${message.tag}</i>
        </div>
    <#else>
        No messages
    </#list>
</@c.page>