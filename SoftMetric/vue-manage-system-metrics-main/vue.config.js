module.exports = {
    baseUrl: './',
    assetsDir: 'static',
    productionSourceMap: false,
    devServer: {
        host: '0.0.0.0',
        port: 8090,
        disableHostCheck: true,
        proxy: {
            '/api':{
                target:'http://localhost:8080',
                changeOrigin:true,
                pathRewrite:{
                    '/api':''
                }
            },
        }
    }
}