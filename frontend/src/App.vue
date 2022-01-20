<template>
  <div id="app">
    <b-navbar toggleable="md" type="dark" variant="info">

      <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

      <b-navbar-brand to="/"><img src="@/assets/Glencairn.png" width="16px" alt=""/> Polet</b-navbar-brand>

      <b-collapse is-nav id="nav_collapse">

        <b-navbar-nav>
          <b-nav-item to="/search">Search</b-nav-item>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
    <router-view/>
  </div>
</template>
<script>
import axios from 'axios'

export default {
  data () {
    return {
      query: '',
      searchResult: []
    }
  },
  methods: {
    doSearch () {
      // console.log('Query: ' + this.query)
      axios.get(`/api/v1/search?q=` + this.query)
        .then(response => {
          // JSON responses are automatically parsed.
          this.searchResult = response.data
        })
        .catch(e => {
          this.errors.push(e)
        })
    }
  }
}
</script>
<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
