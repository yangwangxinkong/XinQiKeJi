<template>
  <ad-detail :is-edit='true'></ad-detail>
</template>

<script>
  import AdDetail from './components/adDetail'

  export default {
    name: 'editForm',
    components: { AdDetail }
  }
</script>
