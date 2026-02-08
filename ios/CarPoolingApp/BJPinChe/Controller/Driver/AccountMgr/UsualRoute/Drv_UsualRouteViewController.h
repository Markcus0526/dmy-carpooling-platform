//
//  Drv_UsualRouteViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UsualRouteCell.h"


@interface Drv_UsualRouteViewController : SuperViewController <UITableViewDataSource, UITableViewDelegate, AccountSvcDelegate, RMSwipeTableViewCellDelegate, RMSwipeTableViewCellDelDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UITableView *              _tableView;
    
    /********************** Member Variable ***********************/
    NSMutableArray *                    mRouteArray;
}

@property (nonatomic, strong) NSIndexPath *selectedIndexPath;

- (IBAction)onClickedBack:(id)sender;

@end
