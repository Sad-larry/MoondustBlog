<template>
  <h1>{{ msg }}</h1>
  <button @click="count++">count is: {{ count }}</button>
  <p>Edit <code>components/HelloWorld.vue</code> to test hot module replacement.</p>
  <h1>信息</h1>
  <h2>NAME: {{name}}</h2>
  <h2>AGE: {{age}}</h2>
  <button @click="sayApp">SayApp</button>
  <button @click="changePros">ChangePros</button>
  <button @click="changeJob">ChangeJob</button>
  <h2>JOB.TYPE: {{job.type}}</h2>
  <h2>JOB.SALARY: {{job.salary}}</h2>
  <h2>JOB.TAG.NAME: {{job.tag.name}}</h2>
</template>

<script>
import {ref, reactive} from "vue"
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  data() {
    return {
      count: 0
    }
  },
  
  setup() {
    let name = ref("app")
    let age = ref(18)
    let job = reactive({
      type: 'JAVA',
      salary: '30k',
      tag: {
        name: "J",
        number: 1
      }
    })
    function sayApp() {
      alert(`my name is ${name}, my age is ${age}.`)
    }
    function changePros() {
      name.value = "ppa"
      age.value = 20
      // console.log(name, age)
    }
    function changeJob() {
      // console.log(job.value)
      // job.value.type = "VUE"
      // job.value.salary = "20k"
      // job.value.tag.name = "K"
      // job.type = "VUE"
      // job.salary = "20k"
      job.tag.name = "K"
      console.log(job)
    }
    return {
      name,
      age,
      job,
      sayApp,
      changePros,
      changeJob
    }
  }
}
</script>
