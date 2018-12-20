<template>
  <detail :is-edit='false'></detail>
</template>

<script>
import Detail from './components/detail'

export default {
  name: 'createForm',
  components: { Detail }
}
</script>

