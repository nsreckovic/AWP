import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  now: number

  constructor() { }

  ngOnInit(): void {
    this.refreshData();
    setInterval(() => { 
        this.refreshData(); 
    }, 10000);
  }

  refreshData(){
    this.now = Date.now();
  }

}
