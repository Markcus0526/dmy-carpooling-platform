//
//  Drv_SearchOrderDetViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMapKit.h"

@interface Drv_WTOrderDetViewController : SuperViewController <BMKMapViewDelegate, OrderSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    
    IBOutlet UIButton *                 _markVerif;
    IBOutlet UILabel *                  _lblVerifDesc;
    IBOutlet UILabel *                  _lblEvalPro;
    IBOutlet UILabel *                  _lblCarpoolCnt;
    

    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblTime;
    IBOutlet UILabel *                  _lblPrice;
    IBOutlet UILabel *                  _lblSysInfoPrice;
    IBOutlet UILabel *                  _lblMidPoint;
    
    IBOutlet BMKMapView *               _mapView;
    
    IBOutlet UIView *                   _vwAlertView;
    
    /********************** Member Variable ***********************/
    
}

@property (nonatomic, readwrite) long           mPassID;
@property (nonatomic, readwrite) long           mOrderID;


- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedPublish:(id)sender;
- (IBAction)onSelectDay:(id)sender;

// st alert view background
- (IBAction)onClickedAlertBG:(id)sender;

@end
