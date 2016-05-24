function getTeam(){
	var team =[];
	var ceo={name:"刘棉",profile:"lm.png",position:"CEO",word:"很高兴遇见你"};
	var core=[
		{name:"胡文姬",profile:"hwj.png",position:"设计总监",word:"越努力&nbsp;&nbsp;越幸福"},
		{name:"刘棉",profile:"ddw.png",position:"技术总监&安卓开发",word:"代码筑城"}
	];
	var first=[
		{name:"李炜颖",profile:"lwy.png",position:"设计",word:"随心而动"},
		{name:"孙长洪",profile:"sch.png",position:"前端&后台",word:"天道酬勤"},
		{name:"鄢慧红",profile:"yhh.png",position:"设计",word:"不因为害怕而不去拥有"},
		{name:"余笃",profile:"yd.png",position:"iOS开发",word:""},
		{name:"杨永坤",profile:"yyk.png",position:"前端&后台",word:"但行好事&nbsp;&nbsp;莫问前程"},
		{name:"张冬阳",profile:"zdy.png",position:"iOS开发",word:"生活不只眼前的苟且"}
	];

	team.push(ceo);
	var i=core.length;//core
	while(i>0){
		var index=Math.floor(Math.random()*i);
		team.push(core[index]);
		i--;
	}	
	var i=first.length;//first
	while(i>0){
		var index=Math.floor(Math.random()*i);
		team.push(first[index]);
		i--;
	}
	return team;
}