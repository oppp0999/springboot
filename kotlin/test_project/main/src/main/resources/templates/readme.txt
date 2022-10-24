1. header- {{title}}은 mustache 템플릿 엔진 문법으로 model에서 넘긴 title이라는 키에 해당하는 값을 꽂아줌.
2. footer - 닫아주는 역할
3. {{> header}}과 {{> footer}}는 마찬가지로 mustache 템플릿 엔진 문법으로 header.mustache 에 있는 내용을 include 하는 것이라고 생각하면 됨.

- 위에서 먼저 보아서 아시겠지만 header는 index의 안으로 include 되기 때문에 최종 html 문서는 모든 mustache를 합치고 model에서 꺼내온 값을 매핑한 후, html로 최종 변환되어 브라우저에 출력되는 것을 알 수 있음.