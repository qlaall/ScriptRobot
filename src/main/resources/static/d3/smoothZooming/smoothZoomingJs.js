
async function getUrlJson(url){
    const res = await fetch(url);
    const result = await res.json();
    return result;
}
getUrlJson("../../static/d3/smoothZooming/smoothZooming.json").then(data=> {
    do3d(data)
})

tree = data => {
    const root = d3.hierarchy(data);
    root.dx = 10;
    root.dy = width / (root.height + 1);
    return d3.tree().nodeSize([root.dx, root.dy])(root);
}

function do3d(data){
    height = 500
    width = 954
    radius = 6
    step = radius * 2
    theta = Math.PI * (3 - Math.sqrt(5))

    data = Array.from({length: 2000}, (_, i) => {
        const r = step * Math.sqrt(i += 0.5), a = theta * i;
        return [
            width / 2 + r * Math.cos(a),
            height / 2 + r * Math.sin(a)
        ];
    })


    let currentTransform = [width / 2, height / 2, height];

    const svg = d3.select("body").append("svg")
        .attr("viewBox", [0, 0, width, height])

    const g = svg.append("g");
    //选择g下的所有a标签，存放data进去，并把data数据join到选择结果上，
    g.selectAll("circle")
        .data(data)
        .join("circle")
        .attr("cx", ([x]) => x)
        .attr("cy", ([, y]) => y)
        .attr("r", radius)
        .attr("fill", (d, i) => d3.interpolateRainbow(i / 360))

    // function transition() {
    //     const d = data[Math.floor(Math.random() * data.length)];
    //     const i = d3.interpolateZoom(currentTransform, [...d, radius * 2 + 1]);
    //
    //     g.transition()
    //         .delay(250)
    //         .duration(i.duration)
    //         .attrTween("transform", () => t => transform(currentTransform = i(t)))
    //         .on("end", transition);
    // }
    //
    // function transform([x, y, r]) {
    //     return `
    //   translate(${width / 2}, ${height / 2})
    //   scale(${height / r})
    //   translate(${-x}, ${-y})
    // `;
    // }


}