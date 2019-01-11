<template>
  <navigation-detail :is-edit='true'></navigation-detail>
</template>

<script>
  import NavigationDetail from './components/navigationDetail'

  export default {
    name: 'createForm',
    components: { NavigationDetail }
  }
</script>

