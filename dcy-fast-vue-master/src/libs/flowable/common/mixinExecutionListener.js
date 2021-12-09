
import executionListenerDialog from '_c/Flowable/nodePanel/property/executionListener'
export default {
  components: {
    executionListenerDialog
  },
  data() {
    return {
      executionListenerLength: 0,
      dialogName: null
    }
  },
  methods: {
    computedExecutionListenerLength() {
      let len = this.element.businessObject.extensionElements.values.length
      this.executionListenerLength = (len === null || len === undefined ? 0 : len)
    },
    finishExecutionListener() {
      if (this.dialogName === 'executionListenerDialog') {
        this.computedExecutionListenerLength()
      }
      this.dialogName = ''
    }
  }
}
