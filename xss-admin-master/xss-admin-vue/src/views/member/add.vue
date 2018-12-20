<template>
  <member-detail :is-edit='false'></member-detail>
</template>

<script>
import MemberDetail from './components/MemberDetail'

export default {
  name: 'createForm',
  components: { MemberDetail }
}
</script>

