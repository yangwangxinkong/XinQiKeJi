<template>
  <role-detail :is-edit='false'></role-detail>
</template>

<script>
import roleDetail from './components/roleDetail'

export default {
  name: 'createForm',
  components: { roleDetail }
}
</script>

