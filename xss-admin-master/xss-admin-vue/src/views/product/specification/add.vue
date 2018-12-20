<template>
  <specification-detail :is-edit='false'></specification-detail>
</template>

<script>
import SpecificationDetail from './components/SpecificationDetail'

export default {
  name: 'createForm',
  components: { SpecificationDetail }
}
</script>

