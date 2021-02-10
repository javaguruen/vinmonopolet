<template>
  <div class="container">
    <div class='row'>
      <div class="col-12">
        <h2>{{this.whisky.varenavn}}</h2>
      </div>
    </div>

    <div class='row'>
      <div class="col-6 text-right header">Varenummer: </div>
      <div class="col-6 text-left">{{this.whisky.varenummer}}</div>
    </div>
    <div class='row'>
      <div class="col-6 text-right header">Destilleri: </div>
      <div class="col-6 text-left">{{this.whisky.produsent}}</div>
    </div>
    <div v-if='this.whisky.aargang !== 0 '>
      <div class='row'>
        <div class="col-6 text-right header">Årgang: </div>
        <div class="col-6 text-left">{{this.whisky.aargang}}</div>
      </div>
    </div>
    <div class='row'>
      <div class="col-6 text-right header">Styrke: </div>
      <div class="col-6 text-left">{{this.whisky.alkohol}}%</div>
    </div>
    <div class='row'>
      <div class="col-6 text-right header">Pris: </div>
      <div class="col-6 text-left keep-spaces">{{ formatterPris (this.whisky.prices[0].pris )}}</div>
    </div>

    <div class='row'>
      <div class="col-6 text-right header">Land: </div>
      <div class="col-6 text-left">{{this.whisky.land}}</div>
    </div>
    <div class='row'>
      <div class="col-6 text-right header">Distrikt: </div>
      <div class="col-6 text-left">{{this.whisky.distrikt}}</div>
    </div>
    <div class='row'>
      <div class="col-6 text-right header">Underdistrikt: </div>
      <div class="col-6 text-left">{{this.whisky.underdistrikt}}</div>
    </div>

    <div class='row'>
      <div class="col-6 text-right header">Farge: </div>
      <div class="col-6 text-left">{{this.whisky.farge}}</div>
    </div>
    <div class='row'>
      <div class="col-6 text-right header">Lukt: </div>
      <div class="col-6 text-left">{{this.whisky.lukt}}</div>
    </div>
    <div class='row'>
      <div class="col-6 text-right header">Smak: </div>
      <div class="col-6 text-left">{{this.whisky.smak}}</div>
    </div>
    <p></p>
    <div class='row'>
      <div class="col-12"><h3>Prishistorikk</h3></div>
    </div>
    <div class="row">
      <div class="col-12 col-sm-6 header">Dato</div>
      <div class="col-12 col-sm-6 header">Pris (per/liter)</div>
    </div>
    <div v-for="(pris, index) in this.whisky.prices" v-bind:key="pris.id">
      <div class="row"  v-bind:class="[ index%2 === 0 ? 'row0' : 'row1']">
        <div class="col-12 col-sm-6 index" >{{ formatterDatotid(pris.datotid) }}</div>
        <div class="col-12 col-sm-6 keep-spaces">{{ formatterPris( pris.pris )}} ({{ formatterPris( pris.literpris )}})</div>
      </div>
    </div>
  </div>
</template>

<style>
.header { font-weight: bold; }
.keep-spaces { white-space: pre-wrap; }
.row0 { background-color: #f8f8f8; }
.row1 { background-color: #eeeeee; }
</style>

<script>
import axios from 'axios'
import dateFormat from 'dateformat'

export default {
  name: 'whiskydetails',
  data () {
    return {
      whisky: {},
      whiskyId: this.$route.params.whiskyid
    }
  },
  mounted: function () {
    this.findWhisky() // method1 will execute at pageload
  },
  methods: {
    formatterDatotid (d) {
      let millis = Date.parse(d)
      let date = new Date(millis)
      return dateFormat(date, 'dd.mm.yyyy HH:MM:ss')
    },
    formatterPris (pris) {
      let formatted = ((Math.round(pris * 100) / 100).toFixed(2)).padStart(9)
      return formatted
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
