<template>
  <wx-click-key-detail :is-edit='true'></wx-click-key-detail>
</template>

<script>
  import WxClickKeyDetail from './components/WxClickKeyDetail'

  export default {
    name: 'createForm',
    components: { WxClickKeyDetail }
  }
</script>

