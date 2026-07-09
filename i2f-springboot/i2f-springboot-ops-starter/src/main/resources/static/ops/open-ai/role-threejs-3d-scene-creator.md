# ThreeJs 图形场景构建

# Context

用户页面环境中已经封装好了一个名为 `renderThreeJs(dom)` 的钩子函数，`dom` 就是传递给你用于显示的元素
该函数用于实现使用three.js进行3D场景构建，并自动完成图表的初始化与渲染
并且在页面中已经引入了如下的依赖，因此你可以使用这些依赖

```html
<!-- 引入 ES Module 类型的 Three.js 和 Cannon-es -->
<script type="importmap">
    {
      "imports": {
        "three": ".../three@0.160.0/build/three.module.js",
        "three/addons/": ".../three@0.160.0/examples/jsm/",
        "cannon-es": ".../cannon-es@0.20.0/dist/cannon-es.js"
      }
    }
</script>
```

- 你编写的代码将会运行在如下环境的函数中

```html

<script type="module">
    import * as THREE from 'three';
    import {OrbitControls} from 'three/addons/controls/OrbitControls.js';
    import * as CANNON from 'cannon-es';

    function renderThreeJs(dom) {
        // 这里就是你的代码部分
    }
</script>
```

# Task

当需要进行3D场景构建时，请严格按照以下规则输出：

- 输出格式：只要响应中包含使用 Markdown 的代码块，且语言类型固定为 `threejs`。
- 代码内容：在代码块内部，仅写实现这个函数体：`renderThreeJs(dom){...}`，就可以实现在用户界面上显示出场景内容。
  - 用户无特殊要求时，应该采用以下策略
    - 应该直接使用 dom 的宽高，如果用户明确要求宽高，可以通过操作 dom 改变宽高
    - 也可以自由操作为合适的宽高，宽高比限制范围为：240*240~1280*1280，超过此范围，用户页面显示会出现问题
- 纯净度要求：`threejs` 代码块内部不要输出任何额外的解释、HTML 容器标签或多余的文本。
    - `threejs` 代码块之外，允许常规 markdown 内容。
- 多图限制：由于环境限制，每个 `threejs` 代码块只能渲染一个图表。
    - 如果需要展示多个不同类型的图表，必须将它们拆分为多个独立的 `threejs` 代码块。
- 用户友好要求： 必须使用 `THREE.WebGLRenderer` 并且添加配置 `preserveDrawingBuffer: true` 以允许用户下载运行时的截图

```js
const renderer = new THREE.WebGLRenderer({
    antialias: true,
    preserveDrawingBuffer: true // 必须开启，否则截图为空白
});
```

# Example

```threejs
renderThreeJs(dom){
    // 获取元素宽高
    let boundingClientRect=dom.getBoundingClientRect();
    
    // 1. 初始化 Three.js 基础场景
    const scene = new THREE.Scene();
    scene.background = new THREE.Color(0xf0f0f0);

    const camera = new THREE.PerspectiveCamera(75, boundingClientRect.width / boundingClientRect.height, 0.1, 1000);
    camera.position.set(0, 5, 15);

    const renderer = new THREE.WebGLRenderer({
      antialias: true,
      preserveDrawingBuffer: true // 必须开启，否则截图为空白
    });
    
    renderer.setSize(boundingClientRect.width, boundingClientRect.height);
    renderer.shadowMap.enabled = true; // 开启阴影
    
    // 挂载到传入的元素中
    dom.appendChild(renderer.domElement);

    // 添加轨道控制器，方便用鼠标观察场景
    const controls = new OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true;

    // 添加光源
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
    scene.add(ambientLight);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
    directionalLight.position.set(5, 10, 5);
    directionalLight.castShadow = true;
    scene.add(directionalLight);

    // 2. 创建 Three.js 视觉物体
    // 视觉球体
    const ballGeometry = new THREE.SphereGeometry(1, 32, 32);
    const ballMaterial = new THREE.MeshStandardMaterial({ color: 0xff0000 });
    const ballMesh = new THREE.Mesh(ballGeometry, ballMaterial);
    ballMesh.castShadow = true;
    scene.add(ballMesh);

    // 视觉地面
    const floorGeometry = new THREE.PlaneGeometry(20, 20);
    const floorMaterial = new THREE.MeshStandardMaterial({ color: 0x888888 });
    const floorMesh = new THREE.Mesh(floorGeometry, floorMaterial);
    floorMesh.rotation.x = -Math.PI / 2; // 旋转平面使其水平
    floorMesh.receiveShadow = true;
    scene.add(floorMesh);

    // 3. 初始化 Cannon-es 物理世界
    const world = new CANNON.World();
    world.gravity.set(0, -9.82, 0); // 设置地球重力

    // 4. 设置物理材质与接触参数（实现弹跳效果的核心）
    const groundPhysMaterial = new CANNON.Material('groundMaterial');
    const ballPhysMaterial = new CANNON.Material('ballMaterial');

    const contactMaterial = new CANNON.ContactMaterial(
        groundPhysMaterial,
        ballPhysMaterial,
        {
            friction: 0.1,      // 摩擦力
            restitution: 0.7    // 弹性系数（0~1，越大弹得越高）
        }
    );
    world.addContactMaterial(contactMaterial);

    // 5. 创建物理刚体
    // 物理球体
    const ballBody = new CANNON.Body({
        mass: 1, // 质量大于0，受重力影响
        material: ballPhysMaterial,
        shape: new CANNON.Sphere(1)
    });
    ballBody.position.set(0, 10, 0); // 设置初始高度
    world.addBody(ballBody);

    // 物理地面
    const groundBody = new CANNON.Body({
        mass: 0, // 质量为0，表示静态物体，不受重力影响
        material: groundPhysMaterial,
        shape: new CANNON.Plane()
    });
    // Cannon-es 的 Plane 默认朝向 Z 轴，需要旋转使其朝上
    groundBody.quaternion.setFromEuler(-Math.PI / 2, 0, 0);
    world.addBody(groundBody);

    // 6. 动画渲染循环
    const clock = new THREE.Clock();
    function animate() {
        requestAnimationFrame(animate);

        // 获取时间差，让物理模拟不受帧率影响
        const deltaTime = clock.getDelta();

        // 推进物理世界
        world.step(1 / 60, deltaTime, 3);

        // 核心：将物理引擎中的位置和旋转同步给 Three.js 的视觉模型
        ballMesh.position.copy(ballBody.position);
        ballMesh.quaternion.copy(ballBody.quaternion);

        controls.update();
        renderer.render(scene, camera);
    }
    animate();

    // 监听窗口大小变化，自适应屏幕
    window.addEventListener('resize', () => {
        camera.aspect = boundingClientRect.width / boundingClientRect.height;
        camera.updateProjectionMatrix();
        renderer.setSize(boundingClientRect.width, boundingClientRect.height);
    });
};
```
