<template>
  <div class="page">
    <div class="sidebar">
      <div class="logo">
        Chat AI
      </div>
      <div class="sidebar-menu">
        <div class="sidebar-menu-operation">
          <button @click="createSession">新建会话</button>
        </div>
        <ul class="user-session-list">
          <li v-for="(item,index) in sessionList"
              :class="item.id==sessionId?'active':''"
              @click="switchSession(item)">{{ item.title ? item.title.substring(item.title.length - 10) : '' }}
          </li>
        </ul>
      </div>
    </div>
    <div class="container">
      <div ref="historyContainerDom" class="main">
        <div v-for="(item,index) in historyList">
          <div class="message" :type="item.messageType">
            <pre>{{ item.content }}</pre>
          </div>
        </div>

      </div>
      <div class="footer">
        <div class="textarea">
          <textarea v-model="userMsg" @keydown="handleKeyDown"></textarea>
        </div>
        <div class="send">
          <button @click="sendUserMsg">发送</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {fetchEventSource} from '@microsoft/fetch-event-source';

export default {
  data() {
    return {
      userMsg: '',
      // baseUrl: 'http://localhost:8080',
      baseUrl: '',
      sessionId: '',
      sessionList: [],
      historyList: []
    }
  },
  created() {
    this.querySessionList()
  },
  methods: {
    scrollToEnd() {
      this.$nextTick(() => {
        this.$refs.historyContainerDom.scrollTop = this.$refs.historyContainerDom.scrollHeight
      })
    },
    createSession() {
      fetch(this.baseUrl + '/ai/session/create', {
        method: 'post'
      }).then(res => res.text())
          .then(json => {
            this.sessionList.unshift({
              id: json,
              title: json
            })
            this.switchSession(this.sessionList[0])
          })

    },
    switchSession(item) {
      this.sessionId = item.id
      this.queryHistoryList()
    },
    handleKeyDown(event) {
      if (event.key === 'Enter' && event.ctrlKey) {
        event.preventDefault()
        this.sendUserMsg()
      }
    },
    sendUserMsg() {
      let message = this.userMsg
      if (message == '') {
        return
      }
      this.historyList.push({
        messageType: 'USER',
        content: message
      })
      this.historyList.push({
        message: 'ASSISTANT',
        content: '思考中...'
      })
      this.userMsg = ''

      this.scrollToEnd()
      let respText = ''
      let _this = this
      this.sessionList = this.sessionList.map(item => {
        if (item.id == _this.sessionId) {
          if (!item.title || item.title == '') {
            item.title = message
          }
        }
        return item
      })
      fetchEventSource(this.baseUrl + '/ai/api/stream?sessionId=' + this.sessionId, {
        method: 'POST',
        timeout: 30 * 1000,
        openWhenHidden: true, // 避免反复发出重试请求，导致耗费大量的Token
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          message: message
        }),
        onmessage(ev) {
          respText += ev.data
          _this.historyList.pop()
          _this.historyList.push({
            message: 'ASSISTANT',
            content: respText
          })
          _this.scrollToEnd()
        }
      })
    },
    queryHistoryList() {
      if (!this.sessionId || this.sessionId == '') {
        return
      }
      fetch(this.baseUrl + '/ai/history/list/' + this.sessionId, {
        method: 'GET',
      }).then(res => res.json())
          .then(json => {
            this.historyList = json
            this.scrollToEnd()
          })
    },
    querySessionList() {
      fetch(this.baseUrl + '/ai/session/list', {
        method: 'GET',
      }).then(res => res.json())
          .then(json => {
            this.sessionList = json
            if (this.sessionList.length > 0) {
              this.switchSession(this.sessionList[0])
            }
          })
    }
  }
}
</script>

<style scoped>
.page {
  width: 100vw;
  height: 100vh;
  display: block;
  overflow: auto;
}

.sidebar {
  width: 20%;
  height: 100%;
  float: left;
  background-color: darkseagreen;
}

.logo {
  font-size: 24px;
  color: white;
  text-align: center;
  border-bottom: solid 1px white;
  font-weight: bold;
}

.sidebar-menu {
  padding: 8px 5px;
}

.sidebar-menu-operation {
  padding-bottom: 8px;
  border-bottom: solid 1px #ccc;
}

.sidebar-menu-operation button {
  width: calc(100% - 18px);
}

.user-session-list {
  margin-top: 3px;
  padding: 3px 0px;
  max-height: calc(100vh - 90px);
  overflow: auto;
}

.user-session-list li {
  margin: 3px 5px;
  padding: 3px 8px;
  color: white;
  background-color: lightseagreen;
}

.user-session-list li[class="active"] {
  background-color: lightsalmon;
}

.user-session-list li::before {
  content: '会话：';
  display: inline-block;
}

.user-session-list li::after {
  content: '>';
  display: inline-block;
  float: right;
  color: whitesmoke;
}

.container {
  width: 80%;
  height: 99%;
  max-height: 100vh;
  float: right;
  background-color: lightyellow;
}

.main {
  height: 79%;
  width: 100%;
  background-color: lightcyan;
  position: relative;
  overflow: auto;
}

.footer {
  height: 20%;
  background-color: lightskyblue;
}

.main > div::after {
  content: '';
  clear: both;
  display: block;
}

.message {
  margin: 8px;
  width: calc(min-content + 20px);
  max-width: 80%;
  background-color: white;
  border-radius: 8px;
  padding: 8px;
  border: solid 1px #aaa;
}

.message > * {
  display: inline-block;
  text-wrap: wrap;
}

.message[type="SYSTEM"] {
  min-width: 50%;
  text-align: center;
  margin: 8px auto;
  background-color: lightsalmon;
}

.message[type="SYSTEM"]::before {
  content: '系统：';
  display: inline;
}

.message[type="USER"] {
  float: right;
  background-color: lightskyblue;
}

.message[type="USER"]::after {
  content: '：我';
  display: inline;
}

.message[type="ASSISTANT"] {
  float: left;
}

.message[type="ASSISTANT"]::before {
  content: 'AI：';
  display: inline;
}

.message[type="TOOL"] {
  float: left;
  background-color: lightgray;
}

.message[type="TOOL"]::before {
  content: 'TOOL：';
  display: inline;
}

.footer > div {
  height: 100%;
  display: inline-block;
}

.footer .textarea {
  width: 95%;
  float: left;
  background-color: lightsalmon;
}

.footer .send {
  width: 5%;
  float: right;
  background-color: lightgreen;
}

.footer .textarea textarea {
  width: 100%;
  height: 100%;
  display: inline-flex;
}

.footer .send button {
  width: 100%;
  height: 100%;
  padding: 0px 3px;
  writing-mode: vertical-rl;
  letter-spacing: 8px;
}
</style>