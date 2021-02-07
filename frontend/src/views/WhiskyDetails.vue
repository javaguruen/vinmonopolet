<template>
  <div class="container">
    <div class='row'>
      <div class="col-12">
        <h2>{{this.whisky.varenavn}}</h2>
      </div>
    </div>
    <div class='row'>
      <div class="col-2">Varenummer: </div>
      <div class="col-1">{{this.whisky.varenummer}}</div>
      <div class="col-2">Navn: </div>
      <div class="col-7">{{this.whisky.varenavn}}</div>
    </div>
    <div class='row'>
      <div class="col-1">{{this.whisky.alkohol}}%</div>
      <div class="col-1">{{this.whisky.volum}}l</div>
      <div class="col-1">Pris: </div>
      <div class="col-1">{{this.whisky.prices[0].pris}},-</div>
      <div class="col-1">Literpris: </div>
      <div class="col-1">{{this.whisky.prices[0].literpris }},-</div>
      <div class="col-2">{{ this.aargang() }} </div>
    </div>
    <div class='row'>
      <div class="col-1">Land: </div>
      <div class="col-1">{{this.whisky.land}}</div>
      <div class="col-2">Distrikt: </div>
      <div class="col-1">{{this.whisky.distrikt}}</div>
      <div class="col-2">Underdistrikt: </div>
      <div class="col-1">{{this.whisky.underdistrikt}}</div>
    </div>
    <div class="row">
      <div class="col-1">produsent:</div>
      <div class="col-5">{{ this.whisky.produsent}}</div>
      <div class="col-1">Grossist: </div>
      <div class="col-5">{{ this.whisky.grossist}}</div>
    </div>
    <div class="row">
      <div class="col-2 text-left">Farge:</div>
      <div class="col-10 text-left">{{ this.whisky.farge}}</div>
    </div>
    <div class="row">
      <div class="col-2 text-left">Lukt:</div>
      <div class="col-10 text-left">{{ this.whisky.lukt}}</div>
    </div>
    <div class="row">
      <div class="col-2 text-left">Smak:</div>
      <div class="col-10 text-left">{{ this.whisky.smak}}</div>
    </div>

    <p></p>
    <div class='row'>
      <div class="col-12 text-left">
        <h3>Prishistorikk</h3>
      </div>
    </div>

    <div class="row">
      <div class="col-4"><b>Dato</b></div>
      <div class="col-4"><b>Pris</b></div>
      <div class="col-4"><b>Literpris</b></div>
    </div>
    <div v-for="pris in this.whisky.prices">
      <div class="row">
        <div class="col-4">{{ pris.datotid}}</div>
        <div class="col-4">{{ pris.pris}}</div>
        <div class="col-4">{{ pris.literpris}}</div>
      </div>
    </div>

  </div>
</template>
<script>

import axios from 'axios'

export default {
  name: 'whiskydetails',
  data () {
    return {
      whisky: {},
      whiskyId: this.$route.params.whiskyid
    }
  },
  mounted: function () {
    console.log('mounted')
    this.findWhisky() // method1 will execute at pageload
  },
  methods: {
    aargang () {
      if (this.whisky.aargang === 0) {
        return ''
      } else {
        return 'Årgang: ' + this.whisky.aargang
      }
    },
    findWhisky () {
      console.log('findWhisky by id ' + this.$route.params.whiskyid)
      axios.get('/api/v1/whiskies/' + this.$route.params.whiskyid, {})
        .then(response => {
          // JSON responses are automatically parsed.
          this.whisky = response.data
          console.log(response.data)
        })
        .catch(e => {
          this.errors.push(e)
        })
    }
  }
}
</script>
