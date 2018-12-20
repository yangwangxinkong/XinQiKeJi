<template>
  <residence-type-detail :is-edit='false'></residence-type-detail>
</template>

<script>
import residenceTypeDetail from './components/residenceTypeDetail'

export default {
  name: 'createForm',
  components: { residenceTypeDetail }
}
</script>
