'use strict'

const path = require('path')

module.exports = {

    plugins: [
        'vux-ui', 'inline-manifest',
        {   
            name: 'duplicate-style',
            options: {
                cssProcessorOptions: {
                    safe: true,
                    zindex: false,
                    autoprefixer: {
                        add: true,
                        "browsers": [
                            "iOS >= 7",
                            "Android >= 4.1"
                        ]
                    }
                }
            }
        },
        {
            name: 'progress-bar',
            envs: ['development']
        },
        {
            name: 'less-theme',
            path: 'src/assets/styles/less/theme.less'
        }
    ]
  
}

