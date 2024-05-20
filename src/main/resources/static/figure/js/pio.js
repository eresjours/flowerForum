/* ----
# Pio Plugin
# By: Dreamer-Paul
# Last Update: 2021.3.3

一个支持更换 Live2D 模型的 Typecho 插件。
---- */



var Paul_Pio = function (prop) {

    var that = this;

    var current = {
        idol: 0,
        menu: document.querySelector(".pio-container .pio-action"),
        canvas: document.getElementById("pio"),
        body: document.querySelector(".pio-container"),
        root: "http://localhost:8887"
    };

    /* - 方法 */
    var modules = {
        // 更换模型

        idol: function () {
            current.idol < (prop.model.length - 1) ? current.idol++ : current.idol = 0;
            return current.idol;
        },
        // 创建内容
        create: function (tag, prop) {
            var e = document.createElement(tag);
            if(prop.class) e.className = prop.class;
            return e;
        },
        // 随机内容
        rand: function (arr) {
            return arr[Math.floor(Math.random() * (arr.length - 1))];
        },
        // 创建对话框方法
        render: function (text) {
            if(text.constructor === Array){
                dialog.innerText = modules.rand(text);
            }
            else if(text.constructor === String){
                dialog.innerText = text;
            }
            dialog.classList.add("active");
            clearTimeout(this.t);
            this.t = setTimeout(function () {
                dialog.classList.remove("active");
            }, 5000);
        },
        // 移除方法
        destroy: function () {
            that.initHidden();
            localStorage.setItem("posterGirl", 0);
        },
        // 是否为移动设备
        isMobile: function () {
            var ua = window.navigator.userAgent.toLowerCase();
            ua = ua.indexOf("mobile") || ua.indexOf("android") || ua.indexOf("ios");

            return window.innerWidth < 500 || ua !== -1;
        }
    };
    this.destroy = modules.destroy;

    var elements = {
        home: modules.create("span", {class: "pio-home"}),
        skin: modules.create("span", {class: "pio-skin"}),
        info: modules.create("span", {class: "pio-info"}),
        sentence: modules.create("span", {class: "pio-sentence"}),
        //close: modules.create("span", {class: "pio-close"}),
        environmentalInfo: modules.create("span", {class: "pio-environmentalInfo"}),
        watering: modules.create("span", {class: "pio-watering"}),
        show: modules.create("div", {class: "pio-show"})
    };

    var dialog = modules.create("div", {class: "pio-dialog"});
    current.body.appendChild(dialog);
    current.body.appendChild(elements.show);


    // 定时轮询获取植物环境数据数据
    function monitorPlantEnvironment() {
        // 记录连续为0的光照强度次数
        let zeroLightCount = 0;

        // 定时轮询获取数据
        function pollData() {
            fetch('/figure/getInfo', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    // 在这里添加其他请求头（如认证信息等）
                }
            })
                .then(response => {
                    // 检查请求是否成功
                    if (!response.ok) {
                        throw new Error('Failed to fetch data');
                    }
                    // 解析 JSON 响应数据
                    return response.json();
                })
                .then(data => {
                    // 成功获取到数据后的处理
                    console.log('成功获取到数据:', data);
                    // 获取data部分
                    const plantData = data.data;
                    plantData.forEach(item => {
                        // ${item.id}: ${item.current_value}
                        switch (item.id) {
                            case 'light':
                                console.log(item.id, item.current_value);
                                if (item.current_value === 1) {
                                    // 光照强度为0时增加计数
                                    zeroLightCount++;
                                    if (zeroLightCount >= 10) {
                                        console.log("长时间缺乏光照！");
                                        // 这里可以执行长时间缺乏光照时的操作，比如提示用户或者触发其他事件
                                        // 重置计数器
                                        zeroLightCount = 0;
                                    }
                                }
                                break;
                            case 'humidity':
                                console.log(item.id, item.current_value);
                                if (item.current_value > 70) {
                                    console.log("水太多了，少浇点！", item.current_value);
                                    // 这里可以执行湿度过高时的操作，比如提示用户或者触发其他事件
                                    // // 获取显示植物信息的元素
                                    // const dialog = document.querySelector('.pio-dialog');
                                    modules.render("水太多了，少浇点！");
                                    // 将每个数据项显示在文本中
                                    // dialog.innerText = item.id + item.current_value;
                                } else if (item.current_value < 20) {
                                    console.log("水太少了，多浇点！", item.current_value);
                                    // 这里可以执行湿度过低时的操作，比如提示用户或者触发其他事件
                                }
                                break;
                            case 'temperature':
                                console.log(item.id, item.current_value);
                                if (item.current_value > 50) {
                                    console.log('太热了！', item.current_value);
                                    // 这里可以执行湿度过高时的操作，比如提示用户或者触发其他事件
                                } else if (item.current_value < 10) {
                                    console.log('有点凉了！', item.current_value);
                                    // 这里可以执行湿度过低时的操作，比如提示用户或者触发其他事件
                                }
                                break;
                        }
                    });
                })
                .catch(error => {
                    // 请求失败时的处理
                    console.error('获取数据失败:', error);
                    // 这里可以处理获取数据失败的情况，比如显示错误信息
                });
        }

        // 设置定时器，每隔一定时间调用 pollData 函数
        const pollingInterval = 50000; // 50秒钟
        setInterval(pollData, pollingInterval);
    }

    /* - 提示操作 */
        var action = {

        // 欢迎
         welcome: function () {

             /*获取用户信息*/
             fetch('/figure', {
                 method: 'GET',
                 headers: {
                     'Content-Type': 'application/json',
                     // 在这里添加其他请求头（如认证信息等）
                 }
             })
                 .then(response => {
                     // 检查请求是否成功
                     if (!response.ok) {
                         throw new Error('Failed to fetch user info');
                     }
                     // 解析 JSON 响应数据
                     return response.json();
                 })
                 .then(data => {
                     // 成功获取用户信息后的处理
                     console.log('成功获取到用户信息:', data);
                     // 开启轮询获取植物信息
                     monitorPlantEnvironment();
                     if(document.referrer !== "" && document.referrer.indexOf(current.root) === -1){
                         var referrer = document.createElement('a');
                         referrer.href = document.referrer;
                         prop.content.referer ? modules.render(prop.content.referer.replace(/%t/, "“" + data.name + "”")) : modules.render("你好 “" + data.name + "” ！");
                     }
                 })
                 .catch(error => {
                     // 请求失败时的处理
                     console.error('获取用户信息失败:', error.message);
                 });

            if(prop.tips){
                var text, hour = new Date().getHours();

                if (hour > 22 || hour <= 5) {
                    text = '你是夜猫子呀？这么晚还不睡觉，明天起的来嘛';
                }
                else if (hour > 5 && hour <= 8) {
                    text = '早上好！';
                }
                else if (hour > 8 && hour <= 11) {
                    text = '上午好！工作顺利嘛，不要久坐，多起来走动走动哦！';
                }
                else if (hour > 11 && hour <= 14) {
                    text = '中午了，工作了一个上午，现在是午餐时间！';
                }
                else if (hour > 14 && hour <= 17) {
                    text = '午后很容易犯困呢，今天的运动目标完成了吗？';
                }
                else if (hour > 17 && hour <= 19) {
                    text = '傍晚了！窗外夕阳的景色很美丽呢，最美不过夕阳红~';
                }
                else if (hour > 19 && hour <= 21) {
                    text = '晚上好，今天过得怎么样？';
                }
                else if (hour > 21 && hour <= 23) {
                    text = '已经这么晚了呀，早点休息吧，晚安~';
                }
                else{
                    text = "奇趣保罗说：这个是无法被触发的吧，哈哈";
                }
                modules.render(text);
            }
            else{
                modules.render(prop.content.welcome || "欢迎来到本站！");
            }
        },
        // 触摸
        touch: function () {
            current.canvas.onclick = function () {
                modules.render(prop.content.touch || ["你在做什么？","工作这么久了休息一会吧", "带我出去晒晒太阳吧"]);
            };
        },
        // 按钮
         buttons: function () {
             // 返回首页
             elements.home.onclick = function () {
                 // location.href用于获取当前页面的 URL，并将浏览器重定向到 current.root 指定的 URL。
                 location.href = current.root;
             };
             elements.home.onmouseover = function () {
                 modules.render(prop.content.home || "点击这里回到首页！");
             };
             current.menu.appendChild(elements.home);

             // 更换模型
             elements.skin.onclick = function () {
                 loadlive2d("pio", prop.model[modules.idol()]);
                 prop.content.skin && prop.content.skin[1] ? modules.render(prop.content.skin[1]) : modules.render("新形象怎么样");
             };
             elements.skin.onmouseover = function () {
                 prop.content.skin && prop.content.skin[0] ? modules.render(prop.content.skin[0]) : modules.render("想看看我的新形象吗？");
             };
             if(prop.model.length > 1) current.menu.appendChild(elements.skin);

             // 关于我
             elements.info.onclick = function () {
                 window.open("https://github.com/eresjours/flowerForum");
             };
             elements.info.onmouseover = function () {
                 modules.render("想了解更多关于我的信息吗？(项目源码地址)");
             };
             current.menu.appendChild(elements.info);

             // 一言
             elements.sentence.onclick = function () {
                 modules.render(fetch('https://v1.hitokoto.cn')
                                     .then(response => response.json())
                                     .then(data => {
                                             const hitokoto = document.querySelector('.pio-dialog')
                                             hitokoto.href = 'https://hitokoto.cn/?uuid=' + data.uuid
                                             hitokoto.innerText = data.hitokoto })
                                     .catch(console.error) )
                  };
                 elements.sentence.onmouseover = function () {
                     modules.render("我从青蛙王子那里听到了不少人生经验。");
                 };
                 current.menu.appendChild(elements.sentence);

             // 植物环境信息
             elements.environmentalInfo.onclick = function () {
                 fetch('/figure/getInfo', {
                     method: 'GET',
                     headers: {
                         'Content-Type': 'application/json',
                         // 在这里添加其他请求头（如认证信息等）
                     }
                 })
                     .then(response => {
                         // 检查请求是否成功
                         if (!response.ok) {
                             throw new Error('Failed to fetch plant info');
                         }
                         // 解析 JSON 响应数据
                         return response.json();
                     })
                     .then(data => {
                         // 成功获取到植物信息后的处理
                         // console.log('成功获取到植物信息:', data);
                         // 获取data部分
                         const plantData = data.data;
                         // 获取显示植物信息的元素
                         const dialog = document.querySelector('.pio-dialog');
                         // 清空 .pio-dialog 元素中的内容
                         dialog.innerText = '';
                         // 将每个数据项显示在文本中
                         plantData.forEach(item => {
                             dialog.innerText += `${item.id}: ${item.current_value}\n`;
                         });
                     })
                     .catch(error => {
                         // 请求失败时的处理
                         console.error('获取植物信息失败:', error.message);
                     });
             };
             elements.environmentalInfo.onmouseover = function () {
                 modules.render("想查看植物的当前信息吗?");
             };
             current.menu.appendChild(elements.environmentalInfo);

             //浇水
             elements.watering.onclick = function () {
                 fetch('/figure/watering', {
                     method: 'GET',
                     headers: {
                         'Content-Type': 'application/json',
                         // 在这里添加其他请求头（如认证信息等）
                     }
                 })
                     .then(response => {
                         // 检查请求是否成功
                         if (!response.ok) {
                             throw new Error('Failed to fetch plant info');
                         }
                         // 解析 JSON 响应数据
                         return response.json();
                     })
                     .then(data => {
                         // 成功获取到返回信息后的处理
                         // console.log('成功获取到返回信息:', data);
                         // 获取data部分
                         if (data.error === "succ") {
                             modules.render("浇水成功", data.error);
                         } else {
                             modules.render("浇水失败", data.error);
                         }
                     })
                     .catch(error => {
                         // 请求失败时的处理
                         console.error('浇水动作失败:', error.message);
                     });
             };
             elements.watering.onmouseover = function () {
                 modules.render("需要浇水吗?");
             };
             current.menu.appendChild(elements.watering);

             // 关闭模型
//             elements.close.onclick = function () {
//                 modules.destroy();
//             };
//             elements.close.onmouseover = function () {
//                 modules.render(prop.content.close || "QWQ 下次再见吧~");
//             };
//             current.menu.appendChild(elements.close);
         },
        custom: function () {
            prop.content.custom.forEach(function (t) {
                if(!t.type) t.type = "default";
                var e = document.querySelectorAll(t.selector);

                if(e.length){
                    for(var j = 0; j < e.length; j++){
                        if(t.type === "read"){
                            e[j].onmouseover = function () {
                                modules.render("想阅读 %t 吗？".replace(/%t/, "“" + this.innerText + "”"));
                            }
                        }
                        else if(t.type === "link"){
                            e[j].onmouseover = function () {
                                modules.render("想了解一下 %t 吗？".replace(/%t/, "“" + this.innerText + "”"));
                            }
                        }
                        else if(t.text){
                            e[j].onmouseover = function () {
                                modules.render(t.text);
                            }
                        }
                    }
                }
            });
        }
    };



    /* - 运行 */
    var begin = {
        static: function () {
            current.body.classList.add("static");
        },
        fixed: function () {
            action.touch(); action.buttons();
        },
        draggable: function () {
            action.touch(); action.buttons();

            var body = current.body;
            body.onmousedown = function (downEvent) {
                var location = {
                    x: downEvent.clientX - this.offsetLeft,
                    y: downEvent.clientY - this.offsetTop
                };

                function move(moveEvent) {
                    body.classList.add("active");
                    body.classList.remove("right");
                    body.style.left = (moveEvent.clientX - location.x) + 'px';
                    body.style.top  = (moveEvent.clientY - location.y) + 'px';
                    body.style.bottom = "auto";
                }

                document.addEventListener("mousemove", move);
                document.addEventListener("mouseup", function () {
                    body.classList.remove("active");
                    document.removeEventListener("mousemove", move);
                });
            };
        }
    };

    // 运行
    this.init = function (onlyText) {
        if(!(prop.hidden&& modules.isMobile())){
            if(!onlyText){
                action.welcome();
                // 初始化，随机加载模型
                // loadlive2d("pio", prop.model[Math.floor(Math.random()*(prop.model.length))]);
                loadlive2d("pio", prop.model[5]);
            }

            switch (prop.mode){
                case "static": begin.static(); break;
                case "fixed":  begin.fixed(); break;
                case "draggable": begin.draggable(); break;
            }

            if(prop.content.custom) action.custom();
        }
    };

    // 隐藏状态
    this.initHidden = function () {
        current.body.classList.add("hidden");
        dialog.classList.remove("active");

        elements.show.onclick = function () {
            current.body.classList.remove("hidden");
            localStorage.setItem("posterGirl", 1);
            that.init();
        }
    }

    localStorage.getItem("posterGirl") == 0 ? this.initHidden() : this.init();
};
