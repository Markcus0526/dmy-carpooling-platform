//
//  FindDriverViewController.h
//  BJPinChe
//
//  Created by CKK on 14-9-16.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMKMapView.h"
@interface FindDriverViewController : UIViewController<BMKMapViewDelegate,LongWayOrderSvcDelegate>
@property (weak, nonatomic) IBOutlet BMKMapView *mapView;
@property (nonatomic) long driverId;
- (IBAction)OnBackClick:(id)sender;
@end
