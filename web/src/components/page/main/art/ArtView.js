import React from "react";
// src/pages/index.js를 통해서 한번에 import 할 수 있도록 함
import MainTab from "../../../common/Tab/MainTab";
import TestImg from "../../../../test/image/img.png"
import Footer from "../FooterComp"
import {Container, GridList, GridListTile, Grid} from "@material-ui/core";

const ArtView = (props) => {
    const imgList = [
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},
        {name: "testImg", img: TestImg, price: 3000},]

    return (
        <div>
            <header>
                <MainTab history={props} tabValue="0"/>
            </header>
            <div style={{display: "flex", marginTop: '200px', marginLeft: "auto"}}>

                <aside style={{width: "278px"}}>
                    <div style={{
                        display: "flex",
                        alignItems: "center"
                    }}>
                        <input type="text" placeholder="크리에이터 또는 제목" style={{
                            height: "24px",
                            lineHeight: "24px",
                            fontSize: "16px",
                            outline: "0",
                            border: "0"
                        }}/>
                        <a style={{
                            display: "inline-block",
                            width: "24px",
                            height: "24px",
                            marginLeft: "12px",
                            background: "url(https://cdn.upbit.com/images/icon_search.504c45c.svg) no-repeat",
                            overflow: "hidden",
                            font: "0/0 Arial",
                            textIndent: "-1000em"
                        }}>검색</a>
                    </div>

                </aside>
                <section style={{width: "954px"}}>

                    <Container>

                        <GridList cellHeight="200px" cols={imgList.length} spacing={20} component="div">

                            {imgList && imgList.length > 0 && imgList.map((test, idx) => (
                                <GridListTile cols={0} rows={0} key={idx} style={{width: '30%'}}>

                                    <Grid>
                                        <img src={test.img} style={{width: '100%', height: '100%'}}/>
                                    </Grid>

                                    <Grid>
                                        <div style={{display:"flex"}}>
                                            <p>{test.name}</p>
                                            <img src="https://cdn.upbit.com/images/icon_like_fill_small.006ba2a.svg" style={{right:"0", marginLeft:"auto"}}/>
                                        </div>
                                    </Grid>

                                    <Grid style={{textAlign: 'left', fontWeight: 'bold'}}>
                                        {test.price + "$"}
                                    </Grid>
                                </GridListTile>
                            ))}
                        </GridList>
                    </Container>

                </section>

            </div>

            <Footer/>
        </div>
    );
}

export default ArtView;
