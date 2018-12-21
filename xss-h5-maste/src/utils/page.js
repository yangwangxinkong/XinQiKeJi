import { get } from '@/api/server'
export default {
    loadingTip:'加载中',
    noDataTip:'我是有底线的',
    onFetching:false,
    dataStatus:0,//0:初始loading;1：有数据且第一页不够一页;2：最后一页
    currentIndex:0,
    params: {
        pageNumber: 1,  //当前页
        pageSize: 10 //每页显示的条数
    },
    lists:[],
    async list(path){
        this.dataStatus = 0;
        await get(path, this.params).then(response => {

            let curLists = response.data.list;
            if(curLists && curLists.length>0){
                if (this.lists && this.params.pageNumber>1) {
                    this.lists = this.lists.concat(curLists);
                } else {
                    this.lists = curLists;
                }
                //console.log("lists.size:" + this.lists.length)
                let totalPages = response.data.total;
                if (totalPages <= this.params.pageNumber) {
                    if(this.params.pageNumber == 1 && curLists.length<this.params.pageSize){
                        this.dataStatus=1;
                    }else{
                        this.dataStatus=2;
                    }
                }else{
                    this.params.pageNumber++;
                  //this.dataStatus=2;
                }
              //console.log("this.dataStatus1:" + this.dataStatus)
            }else{
                this.dataStatus=2;
                this.lists=[];
                this.noDataTip = "没有数据";
            }
        });
    }
}
