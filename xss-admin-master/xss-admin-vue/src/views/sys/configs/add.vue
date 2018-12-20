<template>
  <detail :is-edit='false'></detail>
</template>

<script>
import detail from './components/detail'

export default {
  name: 'editForm',
  components: { detail }
}
</script>

