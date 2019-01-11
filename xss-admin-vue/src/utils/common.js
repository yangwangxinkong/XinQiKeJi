import store from '@/store'
import router from '@/router'
export function goback(path) {
    let selectedTag
    store.state.tagsView.visitedViews.forEach(tag=>{
        if(tag.fullPath==path){ //  
           selectedTag=tag
           store.dispatch('delVisitedViews', selectedTag).then((views) => {
                    const latestView = views.slice(-1)[0]
                    if (latestView) {
                        router.push(latestView)
                    } else {
                        router.push('/')
                    }
                })
            }
      })
}