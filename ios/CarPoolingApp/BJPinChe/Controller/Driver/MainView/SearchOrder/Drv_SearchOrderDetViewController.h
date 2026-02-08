//
//  Drv_SearchOrderDetViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMapKit.h"

@interface Drv_SearchOrderDetViewController : SuperViewController <BMKMapViewDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;

    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblTime;
    IBOutlet UILabel *                  _lblPrice;
    IBOutlet UILabel *                  _lblSysInfoPrice;
    IBOutlet UILabel *                  _lblMidPoint;
    
    IBOutlet BMKMapView *               _mapView;
    
    /********************** Member Variable ***********************/
    
}

@property (nonatomic, readwrite) long           mPassID;


- (IBAction)onClickedBack:(id)sender;

@end
