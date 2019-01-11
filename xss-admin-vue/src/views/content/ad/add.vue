<template>
  <ad-detail :is-edit='false'></ad-detail>
</template>

<script>
  import AdDetail from './components/adDetail'

  export default {
    name: 'createForm',
    components: { AdDetail }
  }
</script>
